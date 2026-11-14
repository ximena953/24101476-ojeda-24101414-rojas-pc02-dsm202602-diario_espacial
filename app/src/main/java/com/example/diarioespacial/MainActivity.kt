package com.example.diarioespacial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.diarioespacial.navigation.NavegacionPrincipal
import com.example.diarioespacial.ui.theme.DiarioEspacialTheme

/** Única Activity: aplica el tema y muestra la navegación principal. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiarioEspacialTheme {
                NavegacionPrincipal()
            }
        }
    }
}
