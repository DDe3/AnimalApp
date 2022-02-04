package com.example.practica.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practica.database.dao.AvistamientoDao
import com.example.practica.database.entidades.Avistamiento

@Database(entities = [Avistamiento::class], version = 1)
abstract class AvistamientoDataBase : RoomDatabase() {
    abstract fun avistamientoDao() : AvistamientoDao
}