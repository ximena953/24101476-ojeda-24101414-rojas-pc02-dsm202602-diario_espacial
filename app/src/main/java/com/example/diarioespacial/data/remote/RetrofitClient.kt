package com.example.diarioespacial.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración compartida de Retrofit para las dos opciones de la app.
 * Se crea una sola vez (by lazy) y la usan los dos repositorios.
 */
object RetrofitClient {
    private const val URL_BASE = "https://api.nasa.gov/"

    // NeoWs puede tardar varios segundos en responder: se amplía el tiempo de espera.
    private val clienteHttp = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val api: NasaApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NasaApi::class.java)
    }
}
