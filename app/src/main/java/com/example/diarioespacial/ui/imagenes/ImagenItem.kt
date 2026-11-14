package com.example.diarioespacial.ui.imagenes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.diarioespacial.data.local.ApodEntity
import com.example.diarioespacial.data.local.esVideo
import com.example.diarioespacial.ui.componentes.BotonFavorito

/** Elemento de la lista: imagen (o "Contenido en video"), título, fecha y favorito. */
@Composable
fun ImagenItem(
    imagen: ApodEntity,
    alPulsar: () -> Unit,
    alCambiarFavorita: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alPulsar)
    ) {
        if (imagen.esVideo || imagen.url == null) {
            // Si es un video no se intenta cargar ninguna imagen.
            AvisoSinImagen(texto = if (imagen.esVideo) "Contenido en video" else "Imagen no disponible")
        } else {
            AsyncImage(
                model = imagen.url,
                contentDescription = imagen.titulo,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = imagen.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(text = imagen.fecha, style = MaterialTheme.typography.bodyMedium)
            }
            BotonFavorito(esFavorito = imagen.esFavorita, alPulsar = alCambiarFavorita)
        }
    }
}

/** Recuadro que ocupa el lugar de la imagen cuando no hay imagen que mostrar. */
@Composable
fun AvisoSinImagen(texto: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
