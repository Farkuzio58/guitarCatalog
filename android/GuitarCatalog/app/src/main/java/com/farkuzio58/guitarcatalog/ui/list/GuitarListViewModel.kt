/*
 * Autor: Alejandro Valle Rodríguez
 * Funcionalidad: ViewModel para el fragmento GuitarListFragment.
 */

package com.farkuzio58.guitarcatalog.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.farkuzio58.guitarcatalog.data.Guitar
import com.farkuzio58.guitarcatalog.data.Id
import com.farkuzio58.guitarcatalog.database.IdRepository
import com.farkuzio58.guitarcatalog.repository.GuitarRepository
import kotlinx.coroutines.launch

class GuitarListViewModel: ViewModel() {
    private val _allGuitar = MutableLiveData<List<Guitar>>()
    val allGuitar: LiveData<List<Guitar>> get() = _allGuitar
    var favourites: List<Id>? = null
    var idRepository = IdRepository()
    var brands: MutableLiveData<List<String>?> = MutableLiveData()
    var prices: MutableLiveData<List<Float>?> = MutableLiveData()
    var reachable = true

    fun getBrands(ip: String) {
        viewModelScope.launch {
            try {
                val result = GuitarRepository.getBrands(ip)
                brands.postValue(result)
            } catch (e: Exception) {
                brands.postValue(null)
            }
        }
    }

    fun load(ip:String, loadFavourites: Boolean, shape:String?, brand:String?, tremolo:String?, strings:String?, price:String?, updatePrice: Boolean, patternToSearch: String?) {
        viewModelScope.launch {
            reachable = GuitarRepository.isHostAccessible(ip)
        }
        val ids = StringBuilder()
        if(loadFavourites){
            favourites = idRepository.selectAll()
            if (favourites != null) {
                favourites!!.forEach {
                    ids.append(it.guitarId.toString()).append(", ")
                }
                if (ids.isEmpty() || ids.toString() == "") {
                    _allGuitar.postValue(emptyList())
                    return
                }
                ids.setLength(ids.length - 2)
            }
            else {
                _allGuitar.postValue(emptyList())
                return
            }
        }

        var shapeTmp: String? = null
        shapeTmp = if (shape == "Otras Formas"){
            "Other"
        } else
            shape
        viewModelScope.launch {
            try {if(loadFavourites) {
                GuitarRepository.getGuitars(ip, ids.toString(), shapeTmp, brand, tremolo, strings, price, patternToSearch)
                    .collect {
                        _allGuitar.postValue(it)
                    }

            } else{
                GuitarRepository.getGuitars(ip,null, shapeTmp, brand, tremolo, strings, price, patternToSearch)
                    .collect {
                        _allGuitar.postValue(it)
                    }
            }
            } catch (e: Exception) {
                _allGuitar.postValue(emptyList())
            }
        }
        if(updatePrice) {
            viewModelScope.launch {
                if (loadFavourites) {
                    val result = GuitarRepository.getPriceRange(ip, ids.toString(), shapeTmp, brand, tremolo, strings)
                    prices.postValue(result)
                } else {
                    val result =
                        GuitarRepository.getPriceRange(ip,null, shapeTmp, brand, tremolo, strings)
                    prices.postValue(result)
                }
            }
        }
    }
}