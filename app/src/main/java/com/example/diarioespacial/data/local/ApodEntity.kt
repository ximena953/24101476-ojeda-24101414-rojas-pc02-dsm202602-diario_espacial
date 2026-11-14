package com.example.diarioespacial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de imágenes APOD. Hay una imagen por día, por eso la fecha es la clave. */
@Entity(tableName = "imagenes_apod")
data class ApodEntity(
    @PrimaryKey val fecha: String, // AAAA-MM-DD
    val titulo: String,
    val explicacion: String,
    val url: String?,
    val urlHd: String?,
    val tipoMedia: String, // "image" o "video"
    val autor: String?,
    val esFavorita: Boolean
)

/** true cuando el elemento de APOD es un video: en ese caso no hay imagen que mostrar. */
val ApodEntity.esVideo: Boolean
    get() = tipoMedia == "video"

