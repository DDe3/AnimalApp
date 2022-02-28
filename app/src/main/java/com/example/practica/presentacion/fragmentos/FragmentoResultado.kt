package com.example.practica.presentacion.fragmentos

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.controladores.adapters.RespuestaController
import com.example.practica.controladores.adapters.ResultController
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.AvistamientoBL
import com.example.practica.logica.FirebaseUpload
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.presentacion.MainActivity
import com.example.practica.presentacion.ResultActivity
import com.example.practica.util.Connection
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*


class FragmentoResultado : Fragment(R.layout.fragmento_resultado) {

    private var _binding: FragmentoResultadoBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel : ResultController by activityViewModels()
    private val fragmentViewModel : RespuestaController by viewModels()


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

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.INVISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSharedPreference()?.let {
            fragmentViewModel.setSharedPreference(it)
        }

        activityViewModel.uri.observe(viewLifecycleOwner) {
            loadWithPicasso(it)
        }

        fragmentViewModel.resultado.observe(viewLifecycleOwner) {
            binding.txtResultado.text = it
            palabraEnlazable(it)
        }

        fragmentViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        fragmentViewModel.changeResultado(activityViewModel.uri.value!!, binding.imgResultado.context)

        fragmentViewModel.accuracy.observe(viewLifecycleOwner) {
            val txt = "Con una seguridad del $it"
            binding.txtPorcentaje.text = txt
        }


        binding.btnGuardarBitacora.setOnClickListener {
            dialogoGuardar()
        }

        binding.btnRegresar.setOnClickListener {
            navegar()
        }

        binding.btnCorregir.setOnClickListener {
            mostrarAlertaTexto()
        }

        binding.autoSave.isChecked = getSharedPreference() == true

        binding.autoSave.setOnCheckedChangeListener { _, isCheked ->
            (activity as ResultActivity).saveSharedPreference(isCheked)
            if (!isCheked) {
                fragmentViewModel.setActive(true)
            } else {
                fragmentViewModel.setActive(false)
            }
        }

        binding.btnTraducir.setOnClickListener {
            traducir()
        }

        fragmentViewModel.isActive.observe(viewLifecycleOwner) {
            binding.btnGuardarBitacora.isEnabled = it
        }


    }

    private fun traducir() {
        lifecycleScope.launch {
            fragmentViewModel.initTranslator(binding.imgResultado.context)
            if (getSharedPreference() == true) {
                dialogoGuardar()
            }
        }
    }

    private fun dialogoGuardar() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Confirmación")
            .setMessage("¿Quieres guardar este avistamiento?")
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("Si") { _, _ ->
                lifecycleScope.launch {
                    val comprobacion = binding.txtResultado.text.toString()
                    if (comprobacion.isBlank()) {
                        AvistamientoBL().saveAvistamiento(requireContext(), comprobacion, activityViewModel.uri.value!!)
                    } else {
                        AvistamientoBL().saveAvistamiento(requireContext(), comprobacion, activityViewModel.uri.value!!)
                    }
                    navegar()
                }
            }
            .show()
    }



    private fun mostrarAlertaTexto() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(binding.autoSave.context)
        builder.setTitle("Corregir este avistamiento")
        val input = EditText(binding.autoSave.context)
        input.hint = "Esto en realidad es un..."
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val inp = input.text.toString()
            FirebaseUpload().uploadAvistamiento(inp.uppercase(), activityViewModel.uri.value!!, requireContext())
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }


    private fun navegar() {
        val i = Intent(binding.btnRegresar.context, MainActivity::class.java)
        startActivity(i)
    }

    fun getSharedPreference() : Boolean? {
        val dbSH = this.activity?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return dbSH?.getBoolean("auto_save", false)
    }

    private fun palabraEnlazable(string: String) {
        binding.txtResultado.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=$string")))
        }
    }

    private fun loadWithPicasso(uri: Uri) {
        Picasso.get().load(uri).fit().into(binding.imgResultado)
    }



}