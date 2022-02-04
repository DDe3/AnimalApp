package com.example.practica.logica

import com.example.practica.casoUso.FactUso

class FactBL {

    fun dameFacts() : List<String> {
        return FactUso().selectFacts().map {it.valor}
    }

}