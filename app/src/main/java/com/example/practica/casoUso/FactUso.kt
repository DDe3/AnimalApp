package com.example.practica.casoUso
import com.example.practica.database.firebase.entidades.Fact
import com.example.practica.util.AnimalAppDB
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class FactUso {


    suspend fun selectFacts() : List<Fact> = withContext(Dispatchers.Default){
        val myList = AnimalAppDB.getFactList()
        if (myList.isEmpty()) {
            AnimalAppDB.getFirebase().child("fact").get().addOnSuccessListener {
                it.children.forEach { x->
                    x.getValue(Fact::class.java)?.let { it1 -> myList.add(it1) }
                }
            }
            AnimalAppDB.setFactList(myList)
        }
        return@withContext myList
    }


}