package com.example.practica.presentacion.fragmentos

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.practica.R
import com.example.practica.controladores.adapters.ResultController
import com.example.practica.presentacion.ResultActivity
import com.example.practica.databinding.FragmentoConfirmarBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.squareup.picasso.Picasso

class FragmentoConfirmar : Fragment(R.layout.fragmento_confirmar) {


    private var _binding: FragmentoConfirmarBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel : ResultController by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentoConfirmarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activityViewModel.uri.observe(viewLifecycleOwner) {
            loadWithPicasso(it)
        }

        // Metodo inline para manejar resultado
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        activityViewModel.changeUri(data?.data!!)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity, "Cancelado", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        binding.btnCargarImagen.setOnClickListener {
            ImagePicker.with(activity as ResultActivity)
                .cropSquare()
                .compress(512)
                .maxResultSize(371,315)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }


        binding.btnConfirmarIdentificar.setOnClickListener {
            activityViewModel.changeFragment(2)
        }

    }

    private fun loadWithPicasso(uri: Uri) {
        Picasso.get().load(uri).fit().into(binding.imgPredict)
    }



}