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
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.AvistamientoBL
import com.example.practica.logica.FirebaseUpload
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.presentacion.MainActivity
import com.example.practica.presentacion.ResultActivity
import com.example.practica.util.Connection
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
    private lateinit var prediccion : String
    private lateinit var uri: Uri

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
        uri = (activity as ResultActivity).uri
        Picasso.get().load(uri).fit().into(binding.imgResultado)
        prediccion = TensorFlowPredict().predecirImagen( uri, binding.btnCorregir.context).uppercase()
        binding.txtResultado.text = prediccion
        palabraEnlazable()
        binding.txtPorcentaje.text = "Con una seguridad del "+TensorFlowPredict().getPorcentaje()


        binding.btnGuardarBitacora.setOnClickListener {
            mostrarAlertaDialogo(binding.btnCorregir.context, uri)
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
                binding.btnGuardarBitacora.isEnabled=true
            }
        }

        binding.btnTraducir.setOnClickListener {
            traducir()
        }


    }

    private fun traducir() {
        lifecycleScope.launch {
            initTranslator(prediccion)
            if (getSharedPreference() == true) {
                mostrarAlertaDialogo(binding.btnCorregir.context, uri)
            }
        }
        palabraEnlazable()
    }


    private fun initTranslator(text: String) {
        val modelManager = RemoteModelManager.getInstance()
        val modeloSpanish = TranslateRemoteModel.Builder(TranslateLanguage.SPANISH).build()

        if (Connection().isOnline(requireContext())) {
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { models ->
                    if (!models.contains(modeloSpanish)) {
                        Toast.makeText(context, "Descargando traductor...", Toast.LENGTH_SHORT).show()
                    }
                    binding.progressBar.visibility = View.VISIBLE
                    binding.btnGuardarBitacora.isEnabled = false
                    binding.txtResultado.text=""
                    val options = TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.SPANISH)
                        .build()
                    val traductor = Translation.getClient(options)
                    lifecycle.addObserver(traductor)
                    val conditions = DownloadConditions.Builder()
                        .requireWifi()
                        .build()
                    Toast.makeText(context, "Traduciendo...", Toast.LENGTH_SHORT).show()
                    traductor.downloadModelIfNeeded(conditions)
                        .addOnSuccessListener {
                            translate(traductor, text)
                        }
                        .addOnFailureListener {
                            binding.txtResultado.text = text
                            Toast.makeText(context, "No se pudo descargar el modelo", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(context, "Esta función requiere conexión a internet por primera vez para descargar el modelo", Toast.LENGTH_SHORT).show()
        }


    }
    //
    private fun translate(translater: Translator, string: String)  {
        val flag = getSharedPreference()

        translater.translate(string)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.INVISIBLE
                binding.txtResultado.text = it.uppercase(Locale.getDefault())
                binding.btnGuardarBitacora.isEnabled = flag != true
                translater.close()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.INVISIBLE
                binding.txtResultado.text = string.uppercase(Locale.getDefault())
                binding.btnGuardarBitacora.isEnabled = flag != true
            }
    }

    private fun mostrarAlertaDialogo(context: Context, uri: Uri)  {

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


    private fun mostrarAlertaTexto() {
        val progress = ProgressDialog(context)
        progress.setTitle("Subiendo avistamiento...")
        progress.show()

        val builder: AlertDialog.Builder = AlertDialog.Builder(binding.autoSave.context)
        builder.setTitle("Corregir este avistamiento")
        val input = EditText(binding.autoSave.context)
        input.setHint("Esto en realidad es un...")
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val inp = input.text.toString()
            FirebaseUpload().uploadAvistamiento(inp.uppercase(), uri, requireContext())
            if (progress.isShowing)
                progress.dismiss()
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

    private fun palabraEnlazable() {
        binding.txtResultado.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + binding.txtResultado.text)))
        }
    }



}