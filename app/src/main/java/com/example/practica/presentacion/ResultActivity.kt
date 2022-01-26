package com.example.practica.presentacion

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.practica.databinding.ActivityResultBinding
import com.example.practica.presentacion.fragmentos.FragmentoConfirmar
import com.example.practica.presentacion.fragmentos.FragmentoResultado


class ResultActivity : AppCompatActivity() {

    lateinit var binding : ActivityResultBinding
    lateinit var uri : Uri
    val inicio = FragmentoConfirmar()
    val identificar = FragmentoResultado()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        uri = intent.getParcelableExtra<Uri>("uri")!!

        cambiarFragmento(FragmentoConfirmar())
    }

    override fun onBackPressed() {
        val fragmento : Fragment? = supportFragmentManager.findFragmentByTag("FragmentoResultado")
        if (fragmento != null && fragmento.isVisible()) {
            cambiarFragmento(inicio)
        } else {
            super.onBackPressed()
        }
    }

    fun cambiarFragmento(fragmento : Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.contenedorFragmentsResult.id, fragmento, fragmento::class.java.simpleName).commit()
    }

}