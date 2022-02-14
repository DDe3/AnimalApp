package com.example.practica.logica

import android.util.Log
import android.view.View
import com.example.practica.databinding.FragmentoResultadoBinding
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.*


class Traduccion {


    fun translate(translater: Translator, string: String) : String?  {
        var hola = "No valio"

        val job =  translater.translate(string)
            .addOnSuccessListener {
                hola = it.uppercase(Locale.getDefault())
                Log.d("traduccion", "*********** SI TRADUJO $string es $it ")
            }
            .addOnFailureListener {
                Log.d("traduccion", it.localizedMessage)
                hola = string.uppercase(Locale.getDefault())
            }
        translater.close()
        if (job.isComplete) {
            hola = job.result
        }
        return hola

    }


}