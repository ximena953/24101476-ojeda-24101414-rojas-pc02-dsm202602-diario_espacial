package com.example.diarioespacial.ui.asteroides

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.diarioespacial.data.local.AsteroideEntity
import com.example.diarioespacial.ui.componentes.BotonFavorito

/** Elemento de la lista de asteroides. Los potencialmente peligrosos se ven en rojo. */
@Composable
fun AsteroideItem(
    asteroide: AsteroideEntity,
    alPulsar: () -> Unit,
    alCambiarFavorito: () -> Unit
) {
    val colores = if (asteroide.esPeligroso) {
        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
    } else {
        CardDefaults.cardColors()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = alPulsar),
        colors = colores
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = asteroide.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (asteroide.esPeligroso) {
                    EtiquetaPeligroso()
                }
                Text(text = "Aproximación: ${asteroide.fechaAproximacion}")
                Text(text = "Diámetro máximo: ${conUnidad(asteroide.diametroMaximoMetros, "m")}")
                Text(text = "Velocidad: ${conUnidad(asteroide.velocidadKmH, "km/h")}")
                Text(text = "Distancia de paso: ${conUnidad(asteroide.distanciaKm, "km")}")
            }
            BotonFavorito(esFavorito = asteroide.esFavorito, alPulsar = alCambiarFavorito)
        }
    }
}

/** Etiqueta roja "Potencialmente peligroso". */
@Composable
fun EtiquetaPeligroso() {
    Text(
        text = "Potencialmente peligroso",
        color = MaterialTheme.colorScheme.onError,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .padding(vertical = 4.dp)
            .background(MaterialTheme.colorScheme.error, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    )
}
