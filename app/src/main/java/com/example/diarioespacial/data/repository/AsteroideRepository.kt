package com.example.diarioespacial.data.repository

import com.example.diarioespacial.BuildConfig
import com.example.diarioespacial.data.local.AsteroideDao
import com.example.diarioespacial.data.local.AsteroideEntity
import com.example.diarioespacial.data.remote.NasaApi
import com.example.diarioespacial.data.remote.dto.aEntities
import com.example.diarioespacial.util.ClaveApiFaltanteException
import kotlinx.coroutines.flow.Flow

/**
 * Único punto de acceso a los datos de asteroides.
 * La pantalla siempre lee desde Room; la API solo sirve para actualizar la tabla.
 */
class AsteroideRepository(
    private val api: NasaApi,
    private val dao: AsteroideDao
) {
    fun observarDesde(fecha: String): Flow<List<AsteroideEntity>> = dao.observarDesde(fecha)

    fun observarFavoritos(): Flow<List<AsteroideEntity>> = dao.observarFavoritos()

    fun observarAsteroide(id: String): Flow<AsteroideEntity?> = dao.observarPorId(id)

    /** Consulta el feed de NeoWs y lo guarda en Room, conservando los favoritos. */
    suspend fun actualizar(fechaInicio: String, fechaFin: String) {
        val clave = BuildConfig.NASA_API_KEY
        if (clave.isBlank()) throw ClaveApiFaltanteException()

        val respuesta = api.obtenerAsteroides(clave, fechaInicio, fechaFin)
        val favoritos = dao.idsFavoritos().toSet()
        val asteroides = respuesta.aEntities()
            .map { asteroide -> asteroide.copy(esFavorito = asteroide.id in favoritos) }
        dao.guardarTodos(asteroides)
    }

    suspend fun cambiarFavorito(asteroide: AsteroideEntity) {
        dao.cambiarFavorito(asteroide.id, !asteroide.esFavorito)
    }
}
