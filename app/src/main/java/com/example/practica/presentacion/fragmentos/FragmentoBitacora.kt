package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica.R
import com.example.practica.casoUso.AvistamientoUso
import com.example.practica.controladores.AvistamientoController
import com.example.practica.controladores.FactController
import com.example.practica.controladores.adapters.AvistamientoAdapter
import com.example.practica.databinding.FragmentoBitacoraBinding
import com.example.practica.databinding.FragmentoInicioBinding
import com.example.practica.entidades.Avistamiento

class FragmentoBitacora : Fragment() {


    private var _binding: FragmentoBitacoraBinding? = null
    private val binding get() = _binding!!
    private lateinit var lista : MutableList<Avistamiento>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoBitacoraBinding.inflate(inflater, container, false)
        lista = AvistamientoUso().dameListaAvistamientos()
        loadAvistamiento(lista)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun loadAvistamiento(items: MutableList<Avistamiento> ) {
        binding.listRecyclerView.layoutManager = LinearLayoutManager(binding.listRecyclerView.context)
        binding.listRecyclerView.adapter = AvistamientoAdapter(items)
    }



}