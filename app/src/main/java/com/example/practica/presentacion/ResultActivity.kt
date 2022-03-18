package com.example.practica.presentacion

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.practica.controladores.adapters.ResultController
import com.example.practica.databinding.ActivityResultBinding
import com.example.practica.presentacion.fragmentos.FragmentoConfirmar
import com.example.practica.presentacion.fragmentos.FragmentoResultado


class ResultActivity : AppCompatActivity() {

    lateinit var binding : ActivityResultBinding
    private val activityViewModel : ResultController by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        if (activityViewModel.uri.value==null) {
            activityViewModel.changeUri(intent.getParcelableExtra("uri")!!)
        }

        activityViewModel.currentFragment.observe(this) {
            cambiarFragmento(it)
        }
        cambiarFragmento(FragmentoConfirmar())
    }

    override fun onBackPressed() {
        val fragmento : Fragment? = supportFragmentManager.findFragmentByTag("FragmentoResultado")
        if (fragmento != null && fragmento.isVisible) {
            activityViewModel.changeFragment(1)
        } else {
            super.onBackPressed()
        }
    }

    fun cambiarFragmento(fragmento : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction
            .replace(binding.contenedorFragmentsResult.id, fragmento, fragmento::class.java.simpleName).commit()
    }

    fun saveSharedPreference(b: Boolean) {
        val dbSH = this.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor = dbSH.edit()
        editor.putBoolean("auto_save", b)
        editor.apply()
    }


}