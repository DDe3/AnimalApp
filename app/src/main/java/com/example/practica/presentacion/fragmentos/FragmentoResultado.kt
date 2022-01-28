package com.example.practica.presentacion.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.practica.R
import com.example.practica.databinding.FragmentoConfirmarBinding
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.presentacion.ResultActivity
import com.squareup.picasso.Picasso

class FragmentoResultado : Fragment(R.layout.fragmento_resultado) {

    private var _binding: FragmentoResultadoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoResultadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = (activity as ResultActivity)
        Picasso.get().load(ctx.uri).fit().into(binding.imgResultado)
        binding.imgResultado.setImageURI(ctx.uri)

        binding.txtResultado.text = TensorFlowPredict().predecirImagen(ctx.uri, ctx)
    }


}