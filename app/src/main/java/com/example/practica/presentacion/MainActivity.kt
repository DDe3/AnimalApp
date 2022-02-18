package com.example.practica.presentacion

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.practica.R
import com.example.practica.databinding.ActivityMainBinding
import com.example.practica.presentacion.fragmentos.FragmentoBitacora
import com.example.practica.presentacion.fragmentos.FragmentoIdentificar
import com.example.practica.presentacion.fragmentos.FragmentoInicio
import com.example.practica.presentacion.fragmentos.FragmentoTutorial
import kotlinx.coroutines.InternalCoroutinesApi

class MainActivity : AppCompatActivity() {

    // Binding
    lateinit var binding : ActivityMainBinding
    // Fragmentos
    val inicio = FragmentoInicio()
    val identificar = FragmentoIdentificar()
    val bitacora = FragmentoBitacora()
    val tutorial = FragmentoTutorial()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        cambiarFragmento(FragmentoInicio())

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.navInicio -> {
                    cambiarFragmento(inicio)
                    true
                }
                R.id.navIdentificar -> {
                    cambiarFragmento(identificar)
                    true
                }
                R.id.navBitacora -> {
                    cambiarFragmento(bitacora)
                    true
                }
                R.id.navTutorial -> {
                    cambiarFragmento(tutorial)
                    true
                }
                else -> false
            }
        }


    }




    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId== R.id.navInicio) {
            super.onBackPressed()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.navInicio
        }
    }


    fun cambiarFragmento(fragmento : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.contenedorFragments.id, fragmento)
            .commit()
    }

    fun hiddenIME(view: View) {
        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}