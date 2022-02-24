package com.example.practica.controladores.adapters

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica.R
import com.example.practica.presentacion.fragmentos.FragmentoBitacora
import com.example.practica.presentacion.fragmentos.FragmentoIdentificar
import com.example.practica.presentacion.fragmentos.FragmentoInicio
import com.example.practica.presentacion.fragmentos.FragmentoTutorial

class ActivityController : ViewModel() {

    private val inicio = FragmentoInicio()
    private val identificar = FragmentoIdentificar()
    private val bitacora = FragmentoBitacora()
    private val tutorial = FragmentoTutorial()

    val currentFragment : MutableLiveData<Fragment> by lazy {
        MutableLiveData<Fragment>()
    }
    val currentTab : MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        currentFragment.postValue(inicio)
        currentTab.postValue(1)
    }

    fun changeFragment(index:Int) : Boolean {
        changeTab(index)
        when(index) {
            R.id.navInicio -> {
                currentFragment.postValue(inicio)
                return true
            }
            R.id.navIdentificar -> {
                currentFragment.postValue(identificar)
                return true
            }
            R.id.navBitacora -> {
                currentFragment.postValue(bitacora)
                return true
            }
            R.id.navTutorial -> {
                currentFragment.postValue(tutorial)
                return true
            }
            else -> return false
        }
    }

    fun changeTab(index:Int) {
        currentTab.postValue(index)
    }



}