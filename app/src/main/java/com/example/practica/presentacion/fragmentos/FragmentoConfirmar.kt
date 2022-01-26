package com.example.practica.presentacion.fragmentos

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.practica.R
import com.example.practica.presentacion.ResultActivity
import com.example.practica.databinding.FragmentoConfirmarBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class FragmentoConfirmar : Fragment(R.layout.fragmento_confirmar) {


    private var _binding: FragmentoConfirmarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoConfirmarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = (activity as ResultActivity)
        binding.imgPredict.setImageURI(ctx.uri)


        // Metodo inline para manejar resultado
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    (activity as ResultActivity).uri = fileUri
                    binding.imgPredict.setImageURI(fileUri)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(activity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                }
            }

        binding.btnCargarImagen.setOnClickListener {
            ImagePicker.with(activity as ResultActivity)
                .cropSquare()
                .compress(1024)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }

        binding.btnConfirmarIdentificar.setOnClickListener {
            val ctx = (activity as ResultActivity)
            ctx.cambiarFragmento(ctx.identificar)
        }

    }



}