package com.example.diarioespacial.ui.asteroides

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.diarioespacial.data.local.AppDatabase
import com.example.diarioespacial.data.local.AsteroideEntity
import com.example.diarioespacial.data.remote.RetrofitClient
import com.example.diarioespacial.data.repository.AsteroideRepository
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

/** Estado de la lista de asteroides que se muestra en pantalla. */
data class AsteroidesUiState(
    val asteroides: List<AsteroideEntity> = emptyList(),
    val soloPeligrosos: Boolean = false,
    val cargando: Boolean = false,
    val error: String? = null
)

/**
 * ViewModel de la opción Asteroides. Lo comparten la lista, el detalle y los favoritos.
 * Expone el estado con StateFlow; los datos siempre vienen de Room.
 */
class AsteroidesViewModel(application: Application) : AndroidViewModel(application) {

    private val repositorio = AsteroideRepository(
        api = RetrofitClient.api,
        dao = AppDatabase.obtener(application).asteroideDao()
    )
    private val rango = Fechas.ultimos7Dias()
    private val soloPeligrosos = MutableStateFlow(false)
    private val cargando = MutableStateFlow(false)
    private val error = MutableStateFlow<String?>(null)
    private var yaConsultado = false

    val uiState: StateFlow<AsteroidesUiState> = combine(
        repositorio.observarDesde(rango.inicio),
        soloPeligrosos,
        cargando,
        error
    ) { asteroides, filtrar, estaCargando, mensaje ->
        AsteroidesUiState(
            // El filtro del interruptor se aplica sobre lo que hay en Room.
            asteroides = if (filtrar) asteroides.filter { it.esPeligroso } else asteroides,
            soloPeligrosos = filtrar,
            cargando = estaCargando,
            error = mensaje
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroidesUiState(cargando = true))

    val favoritos: StateFlow<List<AsteroideEntity>> = repositorio.observarFavoritos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun observarAsteroide(id: String): Flow<AsteroideEntity?> = repositorio.observarAsteroide(id)

    fun cambiarFiltro(activo: Boolean) {
        soloPeligrosos.value = activo
    }

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

    fun cambiarFavorito(asteroide: AsteroideEntity) {
        viewModelScope.launch { repositorio.cambiarFavorito(asteroide) }
    }
}
