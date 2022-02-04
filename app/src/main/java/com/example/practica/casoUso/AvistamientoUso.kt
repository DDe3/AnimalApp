package com.example.practica.casoUso

import com.example.practica.database.entidades.Avistamiento
import com.example.practica.util.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AvistamientoUso() {




//    val lista : List<Avistamiento> = listOf( Avistamiento(0,"Gato","Hoy") ,
//        Avistamiento(0,"Gato 2","Ayer"),
//        Avistamiento(0,"Gato 3","Anteayer"),
//
//    )


    suspend fun getAllAvistamientos() : List<Avistamiento> = withContext(Dispatchers.IO) {
//        val db = Animal.getDatabase()
//        return db.avistamientoDao().getAllAvistamientos()
        return@withContext Animal.getDatabase().avistamientoDao().getAllAvistamientos()
    }

    suspend fun saveAvistamiento(avistamiento: Avistamiento) = withContext(Dispatchers.IO) {
        Animal.getDatabase().avistamientoDao().insertAvistamiento(avistamiento)
    }

    fun deleteAvistamiento(avistamiento: Avistamiento) {
        Animal.getDatabase().avistamientoDao().deleteAvistamiento(avistamiento)
    }




}