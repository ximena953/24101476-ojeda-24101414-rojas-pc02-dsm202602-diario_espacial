package com.example.diarioespacial.data.remote.dto

import com.example.diarioespacial.data.local.ApodEntity
import com.google.gson.annotations.SerializedName

/**
 * Un elemento de la respuesta de APOD, tal como llega de la API.
 * Todos los campos son nulos por si la API no los envía (hdurl y copyright son opcionales).
 */
data class ApodDto(
    @SerializedName("date") val fecha: String?,
    @SerializedName("title") val titulo: String?,
    @SerializedName("explanation") val explicacion: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val urlHd: String?,
    @SerializedName("media_type") val tipoMedia: String?,
    @SerializedName("copyright") val autor: String?
)

/**
 * Conversión explícita DTO -> Entity de Room.
 * Si falta la fecha (la clave primaria), el elemento se descarta devolviendo null.
 */
fun ApodDto.aEntity(): ApodEntity? {
    val fechaImagen = fecha ?: return null
    return ApodEntity(
        fecha = fechaImagen,
        titulo = titulo ?: "Sin título",
        explicacion = explicacion ?: "",
        url = url,
        urlHd = urlHd,
        tipoMedia = tipoMedia ?: "image",
        autor = autor?.trim()?.ifBlank { null },
        esFavorita = false
    )
}
