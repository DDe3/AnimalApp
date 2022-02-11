package com.example.practica.presentacion.fragmentos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.AvistamientoBL
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.logica.Traduccion
import com.example.practica.presentacion.MainActivity
import com.example.practica.presentacion.ResultActivity
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.log


class FragmentoResultado : Fragment(R.layout.fragmento_resultado) {

    private var _binding: FragmentoResultadoBinding? = null
    private val binding get() = _binding!!
    private lateinit var prediccion : String

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
            prediccion = TensorFlowPredict().predecirImagen( uri, binding.btnCorregir.context)
            initTranslator(prediccion)
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
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()
        val traductor = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()


        traductor.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    lifecycleScope.launch(Dispatchers.Main) {
                        var traduccion = withContext(Dispatchers.IO) {
                            Traduccion().translate( traductor, text)
                        }
                        binding.txtResultado.text = traduccion
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                }
                .addOnFailureListener {
                    binding.txtResultado.text = text
                }


        /***************************************************/

        //Log.d("INITRANSLATOR", "**************  $traduccion")
    }
//
//    private fun translate(translater: Translator, string: String)  {
//        val flag = getSharedPreference()
//
//        var hola = "No valio"
//
//        val job =  translater.translate(string)
//            .addOnSuccessListener {
//                hola = it.uppercase(Locale.getDefault())
//                Log.d("traduccion", "*********** SI TRADUJO $string es $it ")
//                binding.progressBar.visibility = View.INVISIBLE
//                binding.txtResultado.text = hola.uppercase(Locale.getDefault())
//                binding.btnGuardarBitacora.isEnabled = flag != true
//                translater.close()
//            }
//            .addOnFailureListener {
//                Log.d("traduccion", it.localizedMessage)
//                hola = string.uppercase(Locale.getDefault())
//                binding.progressBar.visibility = View.INVISIBLE
//                binding.txtResultado.text = hola.uppercase(Locale.getDefault())
//                binding.btnGuardarBitacora.isEnabled = flag != true
//            }
//
//    }

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
                    val comprobacion = binding.txtResultado.text.toString()
                    if (comprobacion.isBlank()) {
                        AvistamientoBL().saveAvistamiento(context, prediccion, uri)
                    } else {
                        AvistamientoBL().saveAvistamiento(context, binding.txtResultado.text.toString(), uri)
                    }
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