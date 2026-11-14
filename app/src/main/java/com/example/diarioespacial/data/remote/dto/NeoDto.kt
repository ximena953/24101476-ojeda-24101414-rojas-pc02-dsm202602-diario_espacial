package com.example.diarioespacial.data.remote.dto

import com.example.diarioespacial.data.local.AsteroideEntity
import com.google.gson.annotations.SerializedName

/**
 * Respuesta de NeoWs Feed. Los asteroides vienen agrupados por fecha:
 * las claves del mapa son fechas y los valores son listas de asteroides.
 */
data class NeoFeedDto(
    @SerializedName("element_count") val cantidad: Int?,
    @SerializedName("near_earth_objects") val asteroidesPorFecha: Map<String, List<NeoDto>?>?
)

/** Un asteroide tal como llega de la API. Todos los campos pueden faltar. */
data class NeoDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val nombre: String?,
    @SerializedName("is_potentially_hazardous_asteroid") val esPeligroso: Boolean?,
    @SerializedName("estimated_diameter") val diametroEstimado: DiametroEstimadoDto?,
    @SerializedName("close_approach_data") val aproximaciones: List<AproximacionDto>?
)

data class DiametroEstimadoDto(
    @SerializedName("meters") val metros: RangoDiametroDto?
)

data class RangoDiametroDto(
    @SerializedName("estimated_diameter_min") val minimo: Double?,
    @SerializedName("estimated_diameter_max") val maximo: Double?
)

/** Datos de la aproximación. La velocidad y la distancia llegan como texto. */
data class AproximacionDto(
    @SerializedName("close_approach_date") val fecha: String?,
    @SerializedName("relative_velocity") val velocidad: VelocidadDto?,
    @SerializedName("miss_distance") val distancia: DistanciaDto?
)

data class VelocidadDto(
    @SerializedName("kilometers_per_hour") val kilometrosPorHora: String?
)

data class DistanciaDto(
    @SerializedName("kilometers") val kilometros: String?
)

/** Aplana el mapa {fecha: [asteroides]} en una sola lista de entidades. */
fun NeoFeedDto.aEntities(): List<AsteroideEntity> =
    asteroidesPorFecha.orEmpty().flatMap { (fecha, asteroides) ->
        asteroides.orEmpty().mapNotNull { asteroide -> asteroide.aEntity(fecha) }
    }

/**
 * Conversión explícita DTO -> Entity de Room.
 * Los textos numéricos se convierten con toDoubleOrNull() para no fallar si vienen mal.
 * Si falta el id (la clave primaria), el asteroide se descarta.
 */
fun NeoDto.aEntity(fechaDelGrupo: String): AsteroideEntity? {
    val idAsteroide = id ?: return null
    val aproximacion = aproximaciones?.firstOrNull()
    return AsteroideEntity(
        id = idAsteroide,
        nombre = nombre ?: "Sin nombre",
        fechaAproximacion = aproximacion?.fecha ?: fechaDelGrupo,
        diametroMinimoMetros = diametroEstimado?.metros?.minimo,
        diametroMaximoMetros = diametroEstimado?.metros?.maximo,
        velocidadKmH = aproximacion?.velocidad?.kilometrosPorHora?.toDoubleOrNull(),
        distanciaKm = aproximacion?.distancia?.kilometros?.toDoubleOrNull(),
        esPeligroso = esPeligroso ?: false,
        esFavorito = false
    )
}
