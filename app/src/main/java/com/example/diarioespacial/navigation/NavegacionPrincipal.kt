package com.example.diarioespacial.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.diarioespacial.ui.imagenes.imagenesGraph

/** Una opción de la barra de navegación inferior. */
private data class OpcionBarra(val ruta: String, val titulo: String, val icono: ImageVector)

private val opciones = listOf(
    OpcionBarra(Rutas.IMAGENES, "Imágenes", Icons.Filled.Star),
    OpcionBarra(Rutas.ASTEROIDES, "Asteroides", Icons.Filled.Place)
)

/**
 * Navegación principal (base común): barra inferior con Imágenes y Asteroides.
 * Cada integrante reemplaza el marcador de su opción por su grafo de navegación.
 */
@Composable
fun NavegacionPrincipal() {
    val navController = rememberNavController()
    val entradaActual by navController.currentBackStackEntryAsState()
    val destinoActual = entradaActual?.destination

    Scaffold(
        // Las pantallas internas manejan sus propios márgenes del sistema.
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                opciones.forEach { opcion ->
                    NavigationBarItem(
                        selected = destinoActual?.hierarchy?.any { it.route == opcion.ruta } == true,
                        onClick = {
                            navController.navigate(opcion.ruta) {
                                // Al cambiar de opción se conserva el estado de cada una.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(imageVector = opcion.icono, contentDescription = null) },
                        label = { Text(text = opcion.titulo) }
                    )
                }
            }
        }
    ) { margenes ->
        NavHost(
            navController = navController,
            startDestination = Rutas.IMAGENES,
            modifier = Modifier.padding(margenes)
        ) {
            imagenesGraph(navController)
            composable(Rutas.ASTEROIDES) { Marcador(texto = "Asteroides: en desarrollo") }
        }
    }
}

@Composable
private fun Marcador(texto: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = texto)
    }
}
