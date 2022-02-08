package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica.casoUso.AvistamientoUso
import com.example.practica.controladores.adapters.AvistamientoAdapter
import com.example.practica.databinding.FragmentoBitacoraBinding
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.logica.AvistamientoBL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoBitacora : Fragment() {


    private var _binding: FragmentoBitacoraBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoBitacoraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val items = withContext(Dispatchers.IO) {
                AvistamientoBL().getListaAvistamientos()
            }
            binding.progressBar.visibility = View.INVISIBLE
            loadAvistamiento(items)
        }
    }

    fun loadAvistamiento(items: MutableList<Avistamiento> ) {
        binding.listRecyclerView.layoutManager =
            LinearLayoutManager(binding.listRecyclerView.context)
        binding.listRecyclerView.adapter = AvistamientoAdapter(items) { borrarAvistamiento(it) }
    }

    private fun borrarAvistamiento(avistamiento: Avistamiento) {
        lifecycleScope.launch {
            AvistamientoBL().deleteAvistamiento(avistamiento)
        }
    }



}