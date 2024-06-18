/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Dao para la clase id.
 */

package com.farkuzio58.guitarcatalog.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.farkuzio58.guitarcatalog.data.Id

@Dao
interface IdDao {
    @Insert
    fun insert(id: Id): Long

    @Query("Select * from id")
    fun selectAll(): List<Id>?

    @Query("Delete from id where guitarId = :id")
    fun delete(id: Int)

    @Query("Delete from id")
    fun deleteAll()
}