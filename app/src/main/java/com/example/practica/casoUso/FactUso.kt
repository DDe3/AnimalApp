package com.example.practica.casoUso

import com.example.practica.entidades.Fact

class FactUso {

    fun selectFacts() : List<Fact> {
        // Aqui se supone que se consulta a DB
        return mutableListOf( Fact(1,"Fact #1"), Fact(2,"Fact #2"),  Fact(3,"Fact #3"),  Fact(4,"Fact #4")   )

    }

}