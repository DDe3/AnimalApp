package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.controladores.adapters.ActivityController
import com.example.practica.controladores.adapters.InicioController
import com.example.practica.databinding.FragmentoInicioBinding
import com.example.practica.logica.FactUpdater
import kotlinx.coroutines.launch
import java.lang.Exception


class FragmentoInicio : Fragment() {

    private var _binding: FragmentoInicioBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel : ActivityController by activityViewModels()
    private val fragmentViewModel : InicioController by viewModels()

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
            activityViewModel.changeFragment(R.id.navIdentificar)
        }

        binding.btnBitacora.setOnClickListener {
            activityViewModel.changeFragment(R.id.navBitacora)
        }

        fragmentViewModel.retLiveData.observe(viewLifecycleOwner) {
            Log.d("ENTRO"," CAMBIO EL DATO")
            binding.randomFact.text = it
        }

        binding.lottieView.setOnClickListener {
            if (!binding.lottieView.isAnimating) {
                lifecycleScope.launch {
                    Log.d("ENTRO"," Tocaste al gato ")
                    binding.lottieView.playAnimation()
                    fragmentViewModel.changeFact()
                }
            }
        }

    }



}