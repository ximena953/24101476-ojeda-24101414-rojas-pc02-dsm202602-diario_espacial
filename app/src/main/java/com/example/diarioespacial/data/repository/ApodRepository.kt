package com.example.diarioespacial.data.repository

import com.example.diarioespacial.BuildConfig
import com.example.diarioespacial.data.local.ApodDao
import com.example.diarioespacial.data.local.ApodEntity
import com.example.diarioespacial.data.remote.NasaApi
import com.example.diarioespacial.data.remote.dto.aEntity
import com.example.diarioespacial.util.ClaveApiFaltanteException
import kotlinx.coroutines.flow.Flow

/**
 * Único punto de acceso a los datos de APOD.
 * La pantalla siempre lee desde Room; la API solo sirve para actualizar la tabla.
 */
class ApodRepository(
    private val api: NasaApi,
    private val dao: ApodDao
) {
    fun observarDesde(fecha: String): Flow<List<ApodEntity>> = dao.observarDesde(fecha)

    fun observarFavoritas(): Flow<List<ApodEntity>> = dao.observarFavoritas()

    fun observarImagen(fecha: String): Flow<ApodEntity?> = dao.observarPorFecha(fecha)

    /** Consulta la API y guarda el resultado en Room, conservando las favoritas. */
    suspend fun actualizar(fechaInicio: String, fechaFin: String) {
        val clave = BuildConfig.NASA_API_KEY
        if (clave.isBlank()) throw ClaveApiFaltanteException()

        val respuesta = api.obtenerImagenes(clave, fechaInicio, fechaFin)
        val favoritas = dao.fechasFavoritas().toSet()
        val imagenes = respuesta
            .mapNotNull { dto -> dto.aEntity() }
            .map { imagen -> imagen.copy(esFavorita = imagen.fecha in favoritas) }
        dao.guardarTodas(imagenes)
    }

    suspend fun cambiarFavorita(imagen: ApodEntity) {
        dao.cambiarFavorita(imagen.fecha, !imagen.esFavorita)
    }
}
