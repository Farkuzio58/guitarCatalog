/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Base de datos para la clase Id.
 */


package com.farkuzio58.guitarcatalog.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.farkuzio58.guitarcatalog.Locator
import com.farkuzio58.guitarcatalog.data.Id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@Database(entities = [Id::class], version = 1, exportSchema = false)
abstract class IdDatabase: RoomDatabase() {
    abstract fun idDao(): IdDao
    companion object {
        @Volatile
        private var INSTANCE: IdDatabase? = null
        fun getInstance(): IdDatabase {
            return INSTANCE ?: synchronized(IdDatabase::class) {
                val instance = buildDatabase()
                INSTANCE = instance
                instance
            }
        }

        private fun buildDatabase(): IdDatabase {
            return Room.databaseBuilder(
                Locator.requireApplication,
                IdDatabase::class.java,
                "Id"
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addCallback(
                    RoomDbInitializer(INSTANCE)
                ).build()
        }
    }
    class RoomDbInitializer(val instance: IdDatabase?) : RoomDatabase.Callback() {

        private val applicationScope = CoroutineScope(SupervisorJob())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            applicationScope.launch(Dispatchers.IO) {
                this.let {
                }
            }
        }
    }
}