package com.example.diarioespacial.data.remote.dto

import androidx.core.text.HtmlCompat
import com.example.diarioespacial.data.local.ApodEntity
import com.google.gson.annotations.SerializedName

/**
 * Un elemento de APOD tal como llega de science.nasa.gov.
 * En este servicio "hdurl" es la imagen y "url" es la página del artículo.
 * La explicación y el autor llegan con etiquetas HTML.
 * Todos los campos son nulos por si la API no los envía.
 */
data class ApodDto(
    @SerializedName("date") val fecha: String?,
    @SerializedName("title") val titulo: String?,
    @SerializedName("explanation") val explicacion: String?,
    @SerializedName("url") val urlArticulo: String?,
    @SerializedName("hdurl") val urlImagen: String?,
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
        titulo = titulo?.let { textoPlano(it) }?.ifBlank { null } ?: "Sin título",
        explicacion = explicacion?.let { textoPlano(it).removePrefix("Explanation:").trim() } ?: "",
        url = urlImagen?.let { miniatura(it) }, // imagen reducida para la lista
        urlHd = urlImagen, // imagen completa para el detalle
        tipoMedia = tipoMedia ?: "image",
        autor = autor?.let { textoPlano(it) }?.ifBlank { null },
        esFavorita = false
    )
}

/** Quita las etiquetas HTML (enlaces, negritas) y deja solo el texto. */
private fun textoPlano(html: String): String =
    HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY).toString().trim()

/**
 * El servidor de imágenes de la NASA permite pedir un ancho menor con ?w=.
 * En la lista se usa una versión de 800 px para que cargue más rápido.
 */
private fun miniatura(urlImagen: String): String =
    urlImagen.substringBefore("?") + "?w=800&fit=clip"
