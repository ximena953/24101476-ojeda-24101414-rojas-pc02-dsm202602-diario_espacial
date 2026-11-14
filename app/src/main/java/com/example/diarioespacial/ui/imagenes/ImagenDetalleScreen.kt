package com.example.diarioespacial.ui.imagenes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.diarioespacial.data.local.esVideo
import com.example.diarioespacial.ui.componentes.BotonFavorito
import com.example.diarioespacial.ui.componentes.MensajeSinDatos

/** Detalle de una imagen: imagen grande, título, fecha, autor y explicación completa. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagenDetalleScreen(
    fecha: String,
    viewModel: ImagenesViewModel,
    volver: () -> Unit
) {
    // La imagen se lee desde Room, igual que en la lista.
    val flujo = remember(fecha) { viewModel.observarImagen(fecha) }
    val imagen by flujo.collectAsStateWithLifecycle(initialValue = null)

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = "Detalle de la imagen") },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    imagen?.let { actual ->
                        BotonFavorito(
                            esFavorito = actual.esFavorita,
                            alPulsar = { viewModel.cambiarFavorita(actual) }
                        )
                    }
                }
            )
        }
    ) { margenes ->
        val actual = imagen
        if (actual == null) {
            MensajeSinDatos(mensaje = "No se encontró la imagen.", modifier = Modifier.padding(margenes))
        } else {
            Column(
                modifier = Modifier
                    .padding(margenes)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen grande: hdurl si existe; si no, url. Si es video, solo el aviso.
                val urlImagen = actual.urlHd ?: actual.url
                if (actual.esVideo || urlImagen == null) {
                    AvisoSinImagen(texto = if (actual.esVideo) "Contenido en video" else "Imagen no disponible")
                } else {
                    AsyncImage(
                        model = urlImagen,
                        contentDescription = actual.titulo,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = actual.titulo,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = actual.fecha, style = MaterialTheme.typography.bodyMedium)
                    actual.autor?.let { autor ->
                        Text(text = "Autor: $autor", style = MaterialTheme.typography.bodyMedium)
                    }
                    Text(
                        text = actual.explicacion.ifBlank { "Sin explicación." },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
    }
}
