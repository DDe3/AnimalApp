package com.example.practica.logica

import com.example.practica.casoUso.FactUso
import kotlin.random.Random


class FactUpdater() {

    private val facts = FactBL().dameFacts()

    fun dameFact() : String {
        val r = Random.nextInt(0, facts.size-1)
        return facts[r]
    }



}