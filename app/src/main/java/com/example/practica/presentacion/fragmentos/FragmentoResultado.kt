package com.example.practica.presentacion.fragmentos

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.practica.R
import com.example.practica.databinding.FragmentoResultadoBinding
import com.example.practica.logica.AvistamientoBL
import com.example.practica.logica.TensorFlowPredict
import com.example.practica.presentacion.MainActivity
import com.example.practica.presentacion.ResultActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlin.concurrent.thread

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
        val ctx = (activity as ResultActivity)
        lifecycleScope.launch {
            binding.txtResultado.text = TensorFlowPredict().predecirImagen( ctx.uri, ctx)
        }
        Picasso.get().load(ctx.uri).fit().into(binding.imgResultado)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ctx = (activity as ResultActivity)
        binding.btnGuardarBitacora.setOnClickListener {

            showAlertWithTextInputLayout(ctx, ctx.uri)


        }

        binding.btnRegresar.setOnClickListener {
            navegar()
        }


    }

    private fun showAlertWithTextInputLayout(context: Context, uri: Uri)  {
        val textInputLayout = TextInputLayout(context)
        textInputLayout.setPadding(
            resources.getDimensionPixelOffset(R.dimen.dp_19), // if you look at android alert_dialog.xml, you will see the message textview have margin 14dp and padding 5dp. This is the reason why I use 19 here
            0,
            resources.getDimensionPixelOffset(R.dimen.dp_19),
            0
        )
        val input = TextInputEditText(context)
        textInputLayout.addView(input)

        val alert = AlertDialog.Builder(context)
            .setTitle("Nombre")
            .setView(textInputLayout)
            .setMessage("Ingresa un nombre para este avistamiento")
            .setPositiveButton("Submit") { dialog, _ ->
                lifecycleScope.launch {
                     AvistamientoBL().saveAvistamiento(context, binding.txtResultado.text.toString(), uri, input.text.toString())
                    navegar()
            }
                dialog.cancel()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }.create()

        alert.show()
    }

    private fun navegar() {
        val i = Intent(binding.btnRegresar.context, MainActivity::class.java)
        startActivity(i)
    }


}