package com.example.diarioespacial.navigation

/** Rutas de Navigation Compose. Cada opción tiene su propio grafo anidado. */
object Rutas {
    // Opción Imágenes (Alumno 1)
    const val IMAGENES = "imagenes"
    const val IMAGENES_LISTA = "imagenes/lista"
    const val IMAGENES_DETALLE = "imagenes/detalle/{fecha}"
    const val IMAGENES_FAVORITAS = "imagenes/favoritas"

    // Opción Asteroides (Alumno 2)
    const val ASTEROIDES = "asteroides"
    const val ASTEROIDES_LISTA = "asteroides/lista"
    const val ASTEROIDES_DETALLE = "asteroides/detalle/{id}"
    const val ASTEROIDES_FAVORITOS = "asteroides/favoritos"

    fun detalleImagen(fecha: String) = "imagenes/detalle/$fecha"
    fun detalleAsteroide(id: String) = "asteroides/detalle/$id"
}
