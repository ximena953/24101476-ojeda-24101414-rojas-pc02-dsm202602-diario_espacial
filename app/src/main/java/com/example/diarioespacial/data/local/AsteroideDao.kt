package com.example.diarioespacial.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/** Consultas de la tabla asteroides. */
@Dao
interface AsteroideDao {

    /** Asteroides desde una fecha, los de aproximación más reciente primero. */
    @Query("SELECT * FROM asteroides WHERE fechaAproximacion >= :desde ORDER BY fechaAproximacion DESC, nombre ASC")
    fun observarDesde(desde: String): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE esFavorito = 1 ORDER BY fechaAproximacion DESC, nombre ASC")
    fun observarFavoritos(): Flow<List<AsteroideEntity>>

    @Query("SELECT * FROM asteroides WHERE id = :id")
    fun observarPorId(id: String): Flow<AsteroideEntity?>

    @Query("SELECT id FROM asteroides WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarTodos(asteroides: List<AsteroideEntity>)

    @Query("UPDATE asteroides SET esFavorito = :favorito WHERE id = :id")
    suspend fun cambiarFavorito(id: String, favorito: Boolean)
}
