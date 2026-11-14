package com.example.diarioespacial.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/** Rango de fechas en el formato AAAA-MM-DD que pide la API de la NASA. */
data class RangoFechas(val inicio: String, val fin: String)

object Fechas {
    private val formato = DateTimeFormatter.ISO_LOCAL_DATE // AAAA-MM-DD

    /**
     * Últimos 7 días: desde hace 6 días hasta hoy.
     * Se usa la hora de Nueva York porque es la que usa la NASA para publicar APOD;
     * así nunca se pide una fecha que todavía no existe.
     */
    fun ultimos7Dias(): RangoFechas {
        val hoy = LocalDate.now(ZoneId.of("America/New_York"))
        return RangoFechas(
            inicio = hoy.minusDays(6).format(formato),
            fin = hoy.format(formato)
        )
    }
}
