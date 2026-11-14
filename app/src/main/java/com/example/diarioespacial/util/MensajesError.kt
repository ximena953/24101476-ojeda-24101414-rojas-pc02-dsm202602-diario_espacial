package com.example.diarioespacial.util

import retrofit2.HttpException
import java.io.IOException

/** Convierte un error de la consulta a la API en un mensaje para el usuario. */
fun mensajeDeError(error: Throwable): String = when (error) {
    is ClaveApiFaltanteException -> error.message ?: "Falta la clave de la API."
    is HttpException -> when (error.code()) {
        401, 403 -> "La clave de la API no es válida. Revisa NASA_API_KEY en local.properties."
        429 -> "Se superó el límite de consultas de la API. Inténtalo más tarde."
        else -> "La API respondió con un error (${error.code()})."
    }
    is IOException -> "No se pudo conectar con la API. Revisa tu conexión a internet."
    else -> "Ocurrió un error inesperado: ${error.message ?: "sin detalle"}"
}

/** Se lanza cuando local.properties no tiene la clave NASA_API_KEY. */
class ClaveApiFaltanteException :
    Exception("Falta la clave NASA_API_KEY en local.properties.")
