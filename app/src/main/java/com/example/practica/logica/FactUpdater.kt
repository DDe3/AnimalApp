package com.example.practica.logica

import com.example.practica.casoUso.FactUso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random


class FactUpdater() {



    suspend fun dameFact() : String? {
        val facts = FactUso().selectFacts()
        val r = Random.nextInt(0, facts.size-1)
        return facts[r].valor
    }

}