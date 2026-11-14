package com.example.diarioespacial.ui.imagenes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarioespacial.data.local.AppDatabase
import com.example.diarioespacial.data.local.ApodEntity
import com.example.diarioespacial.data.remote.RetrofitClient
import com.example.diarioespacial.data.repository.ApodRepository
import com.example.diarioespacial.util.Fechas
import com.example.diarioespacial.util.mensajeDeError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Estado de la lista de imágenes que se muestra en pantalla. */
data class ImagenesUiState(
    val imagenes: List<ApodEntity> = emptyList(),
    val cargando: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel de la opción Imágenes. Lo comparten la lista, el detalle y las favoritas.
 * Expone el estado con StateFlow; los datos siempre vienen de Room.
 */
class ImagenesViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = ApodRepository(
        api = RetrofitClient.api,
        dao = AppDatabase.obtener(application).apodDao()
    )
    private val rango = Fechas.ultimos7Dias()
    private val cargando = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)
    private var yaConsultado = false

    val uiState: StateFlow<ImagenesUiState> = combine(
        repositorio.observarDesde(rango.inicio),
        cargando,
        error
    ) { imagenes, estaCargando, mensaje ->
        ImagenesUiState(imagenes = imagenes, cargando = estaCargando, error = mensaje)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ImagenesUiState(cargando = true))

    val favoritas: StateFlow<List<ApodEntity>> = repositorio.observarFavoritas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observarImagen(fecha: String): Flow<ApodEntity?> = repositorio.observarImagen(fecha)

    /** Consulta la API solo la primera vez que se abre la lista. */
    fun consultarSiHaceFalta() {
        if (!yaConsultado) {
            yaConsultado = true
            actualizar()
        }
    }

    fun actualizar() {
        viewModelScope.launch {
            cargando.value = true
            error.value = null
            try {
                repositorio.actualizar(rango.inicio, rango.fin)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // Si la API falla, la app no se cierra: se muestra el mensaje y lo guardado en Room.
                error.value = mensajeDeError(e)
            } finally {
                cargando.value = false
            }
        }
    }

    fun cambiarFavorita(imagen: ApodEntity) {
        viewModelScope.launch { repositorio.cambiarFavorita(imagen) }
    }
}
