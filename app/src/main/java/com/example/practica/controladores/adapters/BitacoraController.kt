package com.example.practica.controladores.adapters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.logica.AvistamientoBL
import kotlinx.coroutines.launch

class BitacoraController : ViewModel() {



    var retLiveData = MutableLiveData<List<Avistamiento>>()

    var isLoading = MutableLiveData<Boolean>().apply {
        value = false
    }


    init {
        viewModelScope.launch {
            getItems()
        }
    }



    suspend fun getItems() {
        isLoading.postValue(true)
        val ret = AvistamientoBL().getListaAvistamientos()
        retLiveData.postValue(ret)
        isLoading.postValue(false)
    }

    suspend fun filterAndLoadItems(p0 : String) : Boolean {
        if (p0.isBlank()) {
            getItems()
        } else {
            retLiveData.postValue(retLiveData.value?.filter {
                it.nombre.contains(p0.uppercase())
            })
        }
        return false
    }







}