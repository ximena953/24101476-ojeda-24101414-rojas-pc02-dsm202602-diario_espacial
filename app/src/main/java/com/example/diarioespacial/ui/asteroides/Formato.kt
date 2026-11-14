package com.example.diarioespacial.ui.asteroides

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

// Máximo dos decimales y separador de miles: 1234567.891 -> "1,234,567.89"
private val formatoNumero = DecimalFormat("#,##0.##", DecimalFormatSymbols(Locale.US))

/** Número redondeado a dos decimales como máximo, con su unidad. */
fun conUnidad(valor: Double?, unidad: String): String =
    if (valor == null) "Sin dato" else "${formatoNumero.format(valor)} $unidad"
