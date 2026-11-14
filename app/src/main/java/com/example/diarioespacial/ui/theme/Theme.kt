package com.example.diarioespacial.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AzulEspacio = Color(0xFF1B3A7A)
private val AzulNoche = Color(0xFF0B1B3F)
private val AzulClaro = Color(0xFFD8E2FF)
private val Naranja = Color(0xFFFFB74D)

private val ColoresClaros = lightColorScheme(
    primary = AzulEspacio,
    onPrimary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulNoche,
    secondary = Naranja,
    onSecondary = Color.Black
)

private val ColoresOscuros = darkColorScheme(
    primary = AzulClaro,
    onPrimary = AzulNoche,
    primaryContainer = AzulEspacio,
    onPrimaryContainer = AzulClaro,
    secondary = Naranja,
    onSecondary = Color.Black
)

/** Tema Material 3 de la aplicación (modo claro y oscuro). */
@Composable
fun DiarioEspacialTheme(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (temaOscuro) ColoresOscuros else ColoresClaros,
        content = content
    )
}
