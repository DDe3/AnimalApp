package com.example.practica.controladores

import android.app.Activity
import android.content.Context
import android.util.Log
import com.example.practica.casoUso.FactUso
import com.example.practica.presentacion.MainActivity
import kotlinx.coroutines.*
import kotlin.random.Random

class FactController {

    fun dameFacts() : List<String> {
        return FactUso().selectFacts().map {it.valor}
    }


}