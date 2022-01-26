package com.example.practica.logica

import com.example.practica.controladores.FactController
import kotlinx.coroutines.*
import kotlin.random.Random


class FactUpdater() {

    private val facts = FactController().dameFacts()

    fun dameFact() : String {
        val r = Random.nextInt(0, facts.size-1)
        return facts[r]
    }


}