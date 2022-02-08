package com.example.practica.casoUso
import com.example.practica.database.firebase.entidades.Fact
import com.example.practica.util.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FactUso {


    suspend fun selectFacts() : List<Fact> = withContext(Dispatchers.Default){
        // Aqui se supone que se consulta a DB
        //return mutableListOf( Fact(1,"Fact #1"), Fact(2,"Fact #2"),  Fact(3,"Fact #3"),  Fact(4,"Fact #4")   )
        val myList = Animal.getFactList()
        if (myList.isEmpty()) {
            Animal.getFirebase().child("fact").get().addOnSuccessListener {
                it.children.forEach { x->
                    x.getValue(Fact::class.java)?.let { it1 -> myList.add(it1) }
                }
            }
        }
        return@withContext myList
    }

}