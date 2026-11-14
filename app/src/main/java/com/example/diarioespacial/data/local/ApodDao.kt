package com.example.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla imagenes_apod. Los Flow avisan a la pantalla cada vez que cambia la tabla. */
@Dao
interface ApodDao {

    /** Imágenes desde una fecha, de la más reciente a la más antigua. */
    @Query("SELECT * FROM imagenes_apod WHERE fecha >= :desde ORDER BY fecha DESC")
    fun observarDesde(desde: String): Flow<List<ApodEntity>>

    @Query("SELECT * FROM imagenes_apod WHERE esFavorita = 1 ORDER BY fecha DESC")
    fun observarFavoritas(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM imagenes_apod WHERE fecha = :fecha")
    fun observarPorFecha(fecha: String): Flow<ApodEntity?>

    /** Fechas marcadas como favoritas, para no perder la marca al guardar datos nuevos. */
    @Query("SELECT fecha FROM imagenes_apod WHERE esFavorita = 1")
    suspend fun fechasFavoritas(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodas(imagenes: List<ApodEntity>)

    @Query("UPDATE imagenes_apod SET esFavorita = :favorita WHERE fecha = :fecha")
    suspend fun cambiarFavorita(fecha: String, favorita: Boolean)
}
