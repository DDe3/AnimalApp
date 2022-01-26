package com.example.practica.casoUso

import com.example.practica.entidades.Avistamiento

class AvistamientoUso {

    val lista : List<Avistamiento> = listOf( Avistamiento(0,"Gato",null,"Hoy") ,
        Avistamiento(0,"Gato 2",null,"Ayer"),
        Avistamiento(0,"Gato 3",null,"Anteayer"),

    )

    fun dameListaAvistamientos() : MutableList<Avistamiento> {

        return lista.toMutableList()

    }
}