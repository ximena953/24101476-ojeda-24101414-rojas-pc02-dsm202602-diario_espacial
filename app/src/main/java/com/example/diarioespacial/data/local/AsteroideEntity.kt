package com.example.diarioespacial.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Tabla de asteroides cercanos. Los valores numéricos ya están convertidos a número. */
@Entity(tableName = "asteroides")
data class AsteroideEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val fechaAproximacion: String, // AAAA-MM-DD
    val diametroMinimoMetros: Double?,
    val diametroMaximoMetros: Double?,
    val velocidadKmH: Double?,
    val distanciaKm: Double?,
    val esPeligroso: Boolean,
    val esFavorito: Boolean
)
