package com.example.diarioespacial.ui.imagenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diarioespacial.ui.componentes.IndicadorCarga
import com.example.diarioespacial.ui.componentes.MensajeSinDatos

/** Lista de imágenes APOD de los últimos 7 días, de la más reciente a la más antigua. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagenesListaScreen(
    viewModel: ImagenesViewModel,
    abrirDetalle: (fecha: String) -> Unit,
    abrirFavoritas: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // Al abrir la opción se consulta la API (solo la primera vez).
    LaunchedEffect(Unit) { viewModel.consultarSiHaceFalta() }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = "Imágenes del espacio") },
                actions = {
                    IconButton(onClick = { viewModel.actualizar() }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Actualizar")
                    }
                    IconButton(onClick = abrirFavoritas) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Mis imágenes favoritas")
                    }
                }
            )
        }
    ) { margenes ->
        Column(modifier = Modifier.padding(margenes).fillMaxSize()) {
            when {
                estado.cargando && estado.imagenes.isEmpty() -> IndicadorCarga()

                estado.imagenes.isEmpty() -> MensajeSinDatos(
                    mensaje = estado.error ?: "No hay imágenes para mostrar.",
                    alReintentar = { viewModel.actualizar() }
                )

                else -> {
                    // Si se actualiza con datos ya guardados, se muestra una barra fina.
                    if (estado.cargando) {
                        LinearProgressIndicator(modifier = Modifier.padding(horizontal = 16.dp))
                    }
                    estado.error?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(estado.imagenes, key = { it.fecha }) { imagen ->
                            ImagenItem(
                                imagen = imagen,
                                alPulsar = { abrirDetalle(imagen.fecha) },
                                alCambiarFavorita = { viewModel.cambiarFavorita(imagen) }
                            )
                        }
                    }
                }
            }
        }
    }
}
