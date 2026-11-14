package com.example.diarioespacial.ui.asteroides

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

/** Navegación interna de la opción Asteroides: lista, detalle y favoritos. */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = Rutas.ASTEROIDES_LISTA, route = Rutas.ASTEROIDES) {

        composable(Rutas.ASTEROIDES_LISTA) { entrada ->
            AsteroidesListaScreen(
                viewModel = viewModelCompartido(navController, entrada),
                abrirDetalle = { id -> navController.navigate(Rutas.detalleAsteroide(id)) },
                abrirFavoritos = { navController.navigate(Rutas.ASTEROIDES_FAVORITOS) }
            )
        }

        composable(
            route = Rutas.ASTEROIDES_DETALLE,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entrada ->
            AsteroideDetalleScreen(
                id = entrada.arguments?.getString("id").orEmpty(),
                viewModel = viewModelCompartido(navController, entrada),
                volver = { navController.popBackStack() }
            )
        }

        composable(Rutas.ASTEROIDES_FAVORITOS) { entrada ->
            AsteroidesFavoritosScreen(
                viewModel = viewModelCompartido(navController, entrada),
                abrirDetalle = { id -> navController.navigate(Rutas.detalleAsteroide(id)) },
                volver = { navController.popBackStack() }
            )
        }
    }
}

/**
 * Las tres pantallas comparten el mismo ViewModel, guardado en el grafo "asteroides".
 * Así la API se consulta una sola vez aunque se entre y salga del detalle.
 */
@Composable
private fun viewModelCompartido(
    navController: NavHostController,
    entrada: NavBackStackEntry
): AsteroidesViewModel {
    val entradaGrafo = remember(entrada) { navController.getBackStackEntry(Rutas.ASTEROIDES) }
    return viewModel(viewModelStoreOwner = entradaGrafo)
}
