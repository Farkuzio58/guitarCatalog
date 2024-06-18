/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: Repositorio para interactual con la base de datos de Id.
 */

package com.farkuzio58.guitarcatalog.database

import com.farkuzio58.guitarcatalog.data.Id

class IdRepository {
    fun insert(id: Id){
        IdDatabase.getInstance().idDao().insert(id)
    }

    fun selectAll(): List<Id>? {
        return IdDatabase.getInstance().idDao().selectAll()
    }

    fun delete(id: Int){
        IdDatabase.getInstance().idDao().delete(id)
    }

    fun deleteAll(){
        IdDatabase.getInstance().idDao().deleteAll()
    }
}