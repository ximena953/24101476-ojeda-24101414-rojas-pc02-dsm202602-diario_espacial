package com.example.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diarioespacial.ui.componentes.BotonFavorito
import com.example.diarioespacial.ui.componentes.MensajeSinDatos

/** Detalle de un asteroide con todos sus datos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroideDetalleScreen(
    id: String,
    viewModel: AsteroidesViewModel,
    volver: () -> Unit
) {
    // El asteroide se lee desde Room, igual que en la lista.
    val flujo = remember(id) { viewModel.observarAsteroide(id) }
    val asteroide by flujo.collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle del asteroide") },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    asteroide?.let { actual ->
                        BotonFavorito(
                            esFavorito = actual.esFavorito,
                            alPulsar = { viewModel.cambiarFavorito(actual) }
                        )
                    }
                }
            )
        }
    ) { margenes ->
        val actual = asteroide
        if (actual == null) {
            MensajeSinDatos(mensaje = "No se encontró el asteroide.", modifier = Modifier.padding(margenes))
        } else {
            Column(
                modifier = Modifier
                    .padding(margenes)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = actual.nombre,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if (actual.esPeligroso) {
                    EtiquetaPeligroso()
                }
                Dato(titulo = "Id", valor = actual.id)
                Dato(titulo = "Fecha de aproximación", valor = actual.fechaAproximacion)
                Dato(titulo = "Diámetro mínimo estimado", valor = conUnidad(actual.diametroMinimoMetros, "m"))
                Dato(titulo = "Diámetro máximo estimado", valor = conUnidad(actual.diametroMaximoMetros, "m"))
                Dato(titulo = "Velocidad relativa", valor = conUnidad(actual.velocidadKmH, "km/h"))
                Dato(titulo = "Distancia de paso", valor = conUnidad(actual.distanciaKm, "km"))
                Dato(
                    titulo = "Potencialmente peligroso",
                    valor = if (actual.esPeligroso) "Sí" else "No"
                )
            }
        }
    }
}

@Composable
private fun Dato(titulo: String, valor: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = titulo, style = MaterialTheme.typography.labelLarge)
        Text(text = valor, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}
