/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: ViewModel para el fragmento GuitarDetailFragment.
 */

package com.farkuzio58.guitarcatalog.ui.detail

import androidx.lifecycle.ViewModel
import com.farkuzio58.guitarcatalog.data.Guitar
import com.farkuzio58.guitarcatalog.data.Id
import com.farkuzio58.guitarcatalog.database.IdRepository
import com.farkuzio58.guitarcatalog.repository.GuitarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class GuitarDetailViewModel: ViewModel() {
    var idRepository = IdRepository()
    var allId = idRepository.selectAll()

    fun isFavourite(id:Int): Boolean{
        if (allId == null)
            return false
        return allId?.any { it.guitarId == id } ?: false
    }

    fun getGuitar(ip: String, id: Int): Guitar? {
        return runBlocking {
            try {
                withContext(Dispatchers.IO) {
                    GuitarRepository.getGuitar(ip, id)
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    fun addFavourite(id:Int){
        idRepository.insert(Id(id))
    }

    fun deleteFavourite(id:Int){
        idRepository.delete(id)
    }


}