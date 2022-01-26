package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practica.presentacion.MainActivity
import com.example.practica.R
import com.example.practica.controladores.FactController
import com.example.practica.databinding.FragmentoInicioBinding
import com.example.practica.entidades.Fact
import com.example.practica.logica.FactUpdater
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.random.Random


class FragmentoInicio : Fragment() {

    private var _binding: FragmentoInicioBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoInicioBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }


    private fun init() {
        binding.btnIdentificar.setOnClickListener {
            val ctx = (activity as MainActivity)
            ctx.cambiarFragmento(ctx.identificar)
            ctx.binding.bottomNavigation.selectedItemId = R.id.navIdentificar
        }

        binding.btnBitacora.setOnClickListener {
            val ctx = (activity as MainActivity)
            ctx.cambiarFragmento(ctx.bitacora)
            ctx.binding.bottomNavigation.selectedItemId = R.id.navBitacora
        }

        binding.btnUpdateFact.setOnClickListener {
            try {
                binding.randomFact.text = FactUpdater().dameFact()
            } catch (e: Exception) {
                binding.randomFact.text = "Oops! Algo salio mal..."
            }
        }
    }


}