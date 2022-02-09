package com.example.practica.presentacion.fragmentos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.AvistamientoBL
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.logica.Translation
import com.example.practica.presentacion.MainActivity
import com.example.practica.presentacion.ResultActivity
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.properties.Delegates


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

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uri = (activity as ResultActivity).uri

        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnGuardarBitacora.isEnabled = false
            initTranslator(TensorFlowPredict().predecirImagen( uri, binding.btnCorregir.context))
            Picasso.get().load(uri).fit().into(binding.imgResultado)
            binding.txtPorcentaje.text = "Con una seguridad del "+TensorFlowPredict().getPorcentaje()
            if (getSharedPreference() == true) {
                showAlertWithTextInputLayout(binding.btnCorregir.context, uri)
            }
        }

        binding.btnGuardarBitacora.setOnClickListener {
            showAlertWithTextInputLayout(binding.btnCorregir.context, uri)
        }

        binding.btnRegresar.setOnClickListener {
            navegar()
        }

        binding.btnCorregir.setOnClickListener {

        }

        binding.autoSave.isChecked = getSharedPreference() == true

        binding.autoSave.setOnCheckedChangeListener { _, isCheked ->
            (activity as ResultActivity).saveSharedPreference(isCheked)
            if (!isCheked) {
                binding.btnGuardarBitacora.isEnabled=true
            }
        }


    }

    private fun initTranslator(text: String) {
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.EN)
            .setTargetLanguage(FirebaseTranslateLanguage.ES)
            .build()
        val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translate(translator, text)
            }
            .addOnFailureListener {
                binding.txtResultado.text = text
            }
    }

    private fun translate(translater: FirebaseTranslator,string: String)  {
        val flag = getSharedPreference()
        translater.translate(string)
            .addOnSuccessListener {
                binding.txtResultado.text = it.uppercase(Locale.getDefault())
            }
            .addOnFailureListener {
                binding.txtResultado.text = string.uppercase(Locale.getDefault())
            }
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnGuardarBitacora.isEnabled = flag != true
        translater.close()

    }

    private fun showAlertWithTextInputLayout(context: Context, uri: Uri)  {

        val title = SpannableString("Confirmación")

        title.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            title.length,
            0
        )
        val dialogo = SpannableString("¿Quieres guardar este avistamiento?")
        dialogo.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            0,
            dialogo.length,
            0
        )
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(dialogo)
            .setCancelable(false)
            .setPositiveButton("Si") { _, _ ->
                lifecycleScope.launch {
                    AvistamientoBL().saveAvistamiento(context, binding.txtResultado.text.toString(), uri)
                    navegar()
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setIcon(R.drawable.ic_upload_icon)
        val alert = builder.create()
        alert.show()
    }

    private fun navegar() {
        val i = Intent(binding.btnRegresar.context, MainActivity::class.java)
        startActivity(i)
    }

    fun getSharedPreference() : Boolean? {
        val dbSH = this.activity?.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return dbSH?.getBoolean("auto_save", false)
    }




}