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

    /** APOD: una imagen astronómica por día entre las dos fechas (AAAA-MM-DD). */
    @GET("planetary/apod")
    suspend fun obtenerImagenes(
        @Query("api_key") apiKey: String,
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): List<ApodDto>

    /** NeoWs Feed: asteroides que pasan cerca de la Tierra (rango máximo de 7 días). */
    @GET("neo/rest/v1/feed")
    suspend fun obtenerAsteroides(
        @Query("api_key") apiKey: String,
        @Query("start_date") fechaInicio: String,
        @Query("end_date") fechaFin: String
    ): NeoFeedDto
}
