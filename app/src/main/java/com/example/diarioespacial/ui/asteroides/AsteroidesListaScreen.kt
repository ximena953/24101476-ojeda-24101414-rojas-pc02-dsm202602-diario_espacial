package com.example.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diarioespacial.ui.componentes.IndicadorCarga
import com.example.diarioespacial.ui.componentes.MensajeSinDatos

/** Lista de asteroides de los últimos 7 días con el filtro de peligrosos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidesListaScreen(
    viewModel: AsteroidesViewModel,
    abrirDetalle: (id: String) -> Unit,
    abrirFavoritos: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    // Al abrir la opción se consulta el feed (solo la primera vez).
    LaunchedEffect(Unit) { viewModel.consultarSiHaceFalta() }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = "Asteroides cercanos") },
                actions = {
                    IconButton(onClick = { viewModel.actualizar() }) {
                        Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Actualizar")
                    }
                    IconButton(onClick = abrirFavoritos) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = "Mis asteroides")
                    }
                }
            )
        }
    ) { margenes ->
        Column(modifier = Modifier.padding(margenes).fillMaxSize()) {
            // Interruptor de filtro en la parte superior de la lista.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Solo potencialmente peligrosos", modifier = Modifier.weight(1f))
                Switch(
                    checked = estado.soloPeligrosos,
                    onCheckedChange = { activo -> viewModel.cambiarFiltro(activo) }
                )
            }

            when {
                estado.cargando && estado.asteroides.isEmpty() -> IndicadorCarga()

                estado.asteroides.isEmpty() -> MensajeSinDatos(
                    mensaje = estado.error ?: if (estado.soloPeligrosos) {
                        "No hay asteroides potencialmente peligrosos en estos días."
                    } else {
                        "No hay asteroides para mostrar."
                    },
                    alReintentar = { viewModel.actualizar() }
                )

                else -> {
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
                        items(estado.asteroides, key = { it.id }) { asteroide ->
                            AsteroideItem(
                                asteroide = asteroide,
                                alPulsar = { abrirDetalle(asteroide.id) },
                                alCambiarFavorito = { viewModel.cambiarFavorito(asteroide) }
                            )
                        }
                    }
                }
            }
        }
    }
}
