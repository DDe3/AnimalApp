package com.example.practica.controladores.adapters

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.practica.logica.TensorFlowPredict
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.*
import java.util.*

class RespuestaController : ViewModel() {


    private val sharedPreference: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val isActive: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val resultado: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val accuracy: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    init {
        isLoading.postValue(false)
        isActive.postValue(true)
        sharedPreference.postValue(false)
    }

    fun setSharedPreference(boolean: Boolean) {
        sharedPreference.postValue(boolean)
    }

    fun changeResultado(uri: Uri, context: Context) {
        changeLoading(true)
        val ret = TensorFlowPredict().predecirImagen(uri, context).uppercase()
        accuracy.postValue(TensorFlowPredict().getPorcentaje())
        resultado.postValue(ret)
        changeLoading(false)
    }

    fun traduction(string: String) {
        resultado.postValue(string)
    }

    fun changeLoading(boolean: Boolean) {
        isLoading.postValue(boolean)
    }

    fun setActive(boolean: Boolean) {
        isActive.postValue(boolean)
    }

    fun initTranslator(context: Context) {
        val holder = resultado.value
        val modelManager = RemoteModelManager.getInstance()
        val modeloSpanish = TranslateRemoteModel.Builder(TranslateLanguage.SPANISH).build()
        modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
            .addOnSuccessListener { models ->
                if (!models.contains(modeloSpanish)) {
                    Toast.makeText(context, "Descargando traductor...", Toast.LENGTH_SHORT).show()
                }
                changeLoading(true)
                setActive(false)
                traduction("")
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.SPANISH)
                    .build()
                val traductor = Translation.getClient(options)
                val conditions = DownloadConditions.Builder()
                    .requireWifi()
                    .build()
                Toast.makeText(context, "Traduciendo...", Toast.LENGTH_SHORT).show()
                traductor.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        translate(traductor, context, holder!!)
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            "Es necesario conexión a Internet por una única vez",
                            Toast.LENGTH_SHORT
                        ).show()
                        traduction(holder!!)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun translate(translater: Translator, context: Context, holder: String) {
        changeLoading(false)
        translater.translate(holder)
            .addOnSuccessListener { s ->
                traduction(s.uppercase())
                setActive(sharedPreference.value != true)
                translater.close()
            }
            .addOnFailureListener { ex ->
                Toast.makeText(context, ex.message, Toast.LENGTH_SHORT).show()
                translater.close()
            }
    }
}