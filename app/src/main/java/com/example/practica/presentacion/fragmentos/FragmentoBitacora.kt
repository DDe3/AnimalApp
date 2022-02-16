package com.example.practica.presentacion.fragmentos

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica.controladores.adapters.AvistamientoAdapter
import com.example.practica.databinding.FragmentoBitacoraBinding
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.logica.AvistamientoBL
import com.example.practica.presentacion.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentoBitacora : Fragment() {


    private var _binding: FragmentoBitacoraBinding? = null
    private val binding get() = _binding!!
    private var items = ArrayList<Avistamiento>().toMutableList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoBitacoraBinding.inflate(inflater, container, false)
        binding.searchView.isIconified = false
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterAndLoadItems(p0 : String) {
        val itemsFiltered = items.filter {
            it.nombre.contains(p0.uppercase())
        }
        loadAvistamiento(itemsFiltered.toMutableList())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    filterAndLoadItems(p0)
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (!p0.isNullOrBlank()) {
                    filterAndLoadItems(p0)
                } else {
                    loadAvistamiento(items)
                }
                return true
            }

        })

        binding.searchView.setOnCloseListener {
            onStart()
            // TODO cerrar el teclado
            binding.searchView.isIconified = false
            false
        }

    }




    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            items = withContext(Dispatchers.IO) {
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