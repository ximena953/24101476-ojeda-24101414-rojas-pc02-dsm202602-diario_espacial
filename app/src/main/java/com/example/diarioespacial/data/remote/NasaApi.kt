package com.example.diarioespacial.data.remote

import com.example.diarioespacial.data.remote.dto.ApodDto
import com.example.diarioespacial.data.remote.dto.NeoFeedDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Endpoints de la API de la NASA (URL base: https://api.nasa.gov/).
 * Cada integrante agrega aquí el endpoint de su opción.
 */
interface NasaApi {

    /**
     * APOD (Astronomy Picture of the Day): las últimas imágenes publicadas.
     * La NASA trasladó APOD a science.nasa.gov; el endpoint antiguo planetary/apod
     * ahora devuelve el logo de la NASA para todas las fechas. Retrofit acepta una URL
     * completa en @GET, así que esta llamada usa ese servicio sin cambiar la URL base.
     * No necesita clave de API.
     */
    @GET("https://science.nasa.gov/wp-json/wp/v2/apod-basic")
    suspend fun obtenerImagenes(
        @Query("per_page") cantidad: Int
    ): List<ApodDto>

    /** NeoWs Feed: asteroides que pasan cerca de la Tierra (rango máximo de 7 días). */
    @GET("neo/rest/v1/feed")
    suspend fun obtenerAsteroides(
        @Query("api_key") apiKey: String,
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): NeoFeedDto
}
