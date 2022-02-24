package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica.controladores.adapters.AvistamientoAdapter
import com.example.practica.controladores.adapters.BitacoraController
import com.example.practica.databinding.FragmentoBitacoraBinding
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.logica.AvistamientoBL
import kotlinx.coroutines.launch

class FragmentoBitacora : Fragment() {


    private var _binding: FragmentoBitacoraBinding? = null
    private val binding get() = _binding!!
    private val bitacoraControllerViewModel : BitacoraController by viewModels()

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchView.clearFocus()
                lifecycleScope.launch {
                    if (p0 != null) {
                        bitacoraControllerViewModel.filterAndLoadItems(p0)
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                lifecycleScope.launch {
                    if (p0 != null) {
                        bitacoraControllerViewModel.filterAndLoadItems(p0)
                    }
                }
                return false
            }

        })

        binding.searchView.setOnCloseListener {
            lifecycleScope.launch {
                bitacoraControllerViewModel.getItems()
            }
            false

        }


        bitacoraControllerViewModel.retLiveData.observe(viewLifecycleOwner, Observer { items ->
            loadRecyclerView(items)
        })

        bitacoraControllerViewModel.isLoading.observe(viewLifecycleOwner) { bool ->
            if (bool) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

    }


    private fun loadRecyclerView(items : List<Avistamiento>) {
        binding.listRecyclerView.layoutManager =
            LinearLayoutManager(binding.listRecyclerView.context)
        binding.listRecyclerView.adapter = AvistamientoAdapter(items) { borrarAvistamiento(it) }
    }


    private fun borrarAvistamiento(avistamiento: Avistamiento) {
        lifecycleScope.launch {
            AvistamientoBL().deleteAvistamiento(avistamiento)
            bitacoraControllerViewModel.getItems()
        }
    }





}