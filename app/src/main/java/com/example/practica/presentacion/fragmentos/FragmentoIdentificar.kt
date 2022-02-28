package com.example.practica.presentacion.fragmentos

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.practica.presentacion.MainActivity
import com.example.practica.R
import com.example.practica.controladores.adapters.ActivityController
import com.example.practica.presentacion.ResultActivity
import com.example.practica.databinding.FragmentoIdentificarBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.coroutines.InternalCoroutinesApi


class FragmentoIdentificar : Fragment() {

    private var _binding: FragmentoIdentificarBinding? = null
    private val binding get() = _binding!!
    private val viewModel : ActivityController by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentoIdentificarBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Cambia de activity al seleccionar una imagen
        val startForProfileImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        //La uri nunca va a ser nula si es que RESULT OK
                        val uri: Uri = data?.data!!
                        val intent = Intent(activity, ResultActivity::class.java)
                        intent.putExtra("uri", uri)
                        Log.d("Identificar:", "Aqui se supone que se cambia de activity")
                        startActivity(intent)
                    }
                    ImagePicker.RESULT_ERROR -> {
                        Toast.makeText(activity, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(activity, "Cancelado", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        binding.btnTutorialIdentificar.setOnClickListener {
            viewModel.changeFragment(R.id.navTutorial)
        }

        binding.imgBtnIdentificar.setOnClickListener {
            ImagePicker.with(requireActivity())
                .cropSquare()
                .compress(512)
                .maxResultSize(371,315)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }



}