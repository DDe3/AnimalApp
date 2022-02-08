package com.example.practica.logica

import android.util.Log
import android.view.View
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import java.util.*

class Translation {

    private var retorno = ""

    private val options = FirebaseTranslatorOptions.Builder()
        .setSourceLanguage(FirebaseTranslateLanguage.EN)
        .setTargetLanguage(FirebaseTranslateLanguage.ES)
        .build()
    private val translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)


    fun traducir(txt: String) : String {
        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                translate(translator, txt)
            }
            .addOnFailureListener {
                Log.d("TRANSLATOR:", "No se descargo el modelo $it")
            }
        Log.d("SAVE:", "Traduje $txt -> $retorno")
        return retorno
    }
    private fun translate(translater: FirebaseTranslator, string: String) {
        translater.translate(string)
            .addOnSuccessListener {
                retorno = it
            }
            .addOnFailureListener {
                Log.d("SAVE:", "$it")
                //retorno = string
            }
        translater.close()

    }

}