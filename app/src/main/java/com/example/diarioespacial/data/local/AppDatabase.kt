package com.example.diarioespacial.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos local SQLite (Room), compartida por las dos opciones.
 * Cada integrante agrega aquí su Entity y su DAO.
 */
@Database(
    entities = [ApodEntity::class, AsteroideEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun apodDao(): ApodDao

    abstract fun asteroideDao(): AsteroideDao

    companion object {
        @Volatile
        private var instancia: AppDatabase? = null

        /** Devuelve siempre la misma instancia (patrón singleton). */
        fun obtener(context: Context): AppDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diario_espacial.db"
                )
                    // Los datos son una copia de la API: si cambia la estructura, se recrean.
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { instancia = it }
            }
        }
    }
}
