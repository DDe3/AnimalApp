package com.example.practica.casoUso

import com.example.practica.database.entidades.Avistamiento
import com.example.practica.util.Animal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AvistamientoUso {


    suspend fun getAllAvistamientos() : List<Avistamiento> = withContext(Dispatchers.IO) {
        return@withContext Animal.getDatabase().avistamientoDao().getAllAvistamientos()
    }

    suspend fun saveAvistamiento(avistamiento: Avistamiento) = withContext(Dispatchers.IO) {
        Animal.getDatabase().avistamientoDao().insertAvistamiento(avistamiento)
    }

    suspend fun deleteAvistamiento(avistamiento: Avistamiento) = withContext(Dispatchers.IO){
        Animal.getDatabase().avistamientoDao().deleteAvistamiento(avistamiento)
    }




}