package com.example.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.diarioespacial.ui.componentes.MensajeSinDatos

/** "Mis asteroides": solo los asteroides marcados como favoritos. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidesFavoritosScreen(
    viewModel: AsteroidesViewModel,
    abrirDetalle: (id: String) -> Unit,
    volver: () -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = { Text(text = "Mis asteroides") },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { margenes ->
        if (favoritos.isEmpty()) {
            MensajeSinDatos(
                mensaje = "Todavía no marcaste ningún asteroide como favorito.",
                modifier = Modifier.padding(margenes)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(margenes),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoritos, key = { it.id }) { asteroide ->
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
