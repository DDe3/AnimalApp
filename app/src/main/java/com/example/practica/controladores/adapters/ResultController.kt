package com.example.practica.controladores.adapters

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica.presentacion.fragmentos.FragmentoConfirmar
import com.example.practica.presentacion.fragmentos.FragmentoResultado

class ResultController : ViewModel() {

    private val inicio = FragmentoConfirmar()   //1
    private val identificar = FragmentoResultado()  //2


    val uri : MutableLiveData<Uri> by lazy {
        MutableLiveData<Uri>()
    }

    val currentFragment : MutableLiveData<Fragment> by lazy {
        MutableLiveData<Fragment>()
    }

    init {
        currentFragment.postValue(inicio)
    }

    fun changeFragment(id: Int) {
        when (id) {
            2 -> {
                currentFragment.postValue(identificar)
            }
            else -> {
                currentFragment.postValue(inicio)
            }
        }
    }

    fun changeUri(newUri : Uri) {
        uri.postValue(newUri)
    }


}