package com.example.practica.util

import android.app.Application
import androidx.room.Room
import com.example.practica.database.AvistamientoDataBase

class Animal : Application() {

    companion object {
        private var db: AvistamientoDataBase? = null

        fun getDatabase(): AvistamientoDataBase {
            return db!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AvistamientoDataBase::class.java, "animal_DB")
            .build()
    }
}