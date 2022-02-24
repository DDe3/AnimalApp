package com.example.practica.controladores.adapters

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica.logica.FactUpdater
import java.lang.Exception

class InicioController : ViewModel() {

    private val TAG = "INICIO CONTROLLER"

    val retLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        retLiveData.postValue("¡Dale al gato para obtener un dato curioso!")
    }

    suspend fun changeFact() {
        try {
            val ret = FactUpdater().dameFact()
            retLiveData.postValue(ret)
        } catch (e: Exception) {
            retLiveData.postValue("El gato dice: ¡Requiero internet para esto!")
            Log.d(TAG, e.message!!)
        }
    }


}