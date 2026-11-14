package com.example.diarioespacial.ui.imagenes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.diarioespacial.navigation.Rutas

/** Navegación interna de la opción Imágenes: lista, detalle y favoritas. */
fun NavGraphBuilder.imagenesGraph(navController: NavHostController) {
    navigation(startDestination = Rutas.IMAGENES_LISTA, route = Rutas.IMAGENES) {

        composable(Rutas.IMAGENES_LISTA) { entrada ->
            ImagenesListaScreen(
                viewModel = viewModelCompartido(navController, entrada),
                abrirDetalle = { fecha -> navController.navigate(Rutas.detalleImagen(fecha)) },
                abrirFavoritas = { navController.navigate(Rutas.IMAGENES_FAVORITAS) }
            )
        }

        composable(
            route = Rutas.IMAGENES_DETALLE,
            arguments = listOf(navArgument("fecha") { type = NavType.StringType })
        ) { entrada ->
            ImagenDetalleScreen(
                fecha = entrada.arguments?.getString("fecha").orEmpty(),
                viewModel = viewModelCompartido(navController, entrada),
                volver = { navController.popBackStack() }
            )
        }

        composable(Rutas.IMAGENES_FAVORITAS) { entrada ->
            ImagenesFavoritasScreen(
                viewModel = viewModelCompartido(navController, entrada),
                abrirDetalle = { fecha -> navController.navigate(Rutas.detalleImagen(fecha)) },
                volver = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Las tres pantallas comparten el mismo ViewModel, guardado en el grafo "imagenes".
 * Así la API se consulta una sola vez aunque se entre y salga del detalle.
 */
@Composable
private fun viewModelCompartido(
    navController: NavHostController,
    entrada: NavBackStackEntry
): ImagenesViewModel {
    val entradaGrafo = remember(entrada) { navController.getBackStackEntry(Rutas.IMAGENES) }
    return viewModel(viewModelStoreOwner = entradaGrafo)
}
