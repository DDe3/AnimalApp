package com.example.practica.util

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.example.practica.casoUso.FactUso
import com.example.practica.database.AvistamientoDataBase
import com.example.practica.database.firebase.entidades.Fact
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AnimalAppDB : Application() {

    companion object {
        private var db: AvistamientoDataBase? = null
        private lateinit var database: DatabaseReference
        private var myList: MutableList<Fact> = mutableListOf()
        private lateinit var dbPreferences : SharedPreferences


        fun getDatabase(): AvistamientoDataBase {
            return db!!
        }

        fun getFirebase(): DatabaseReference {
            return database
        }

        fun getFactList(): MutableList<Fact> {
            return myList
        }

        fun setFactList(facts: MutableList<Fact>) {
            this.myList = facts
        }

        fun getPreferencias() : SharedPreferences {
            return dbPreferences
        }


    }

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AvistamientoDataBase::class.java, "animal_DB")
            .build()
        database = FirebaseDatabase.getInstance().reference;
        dbPreferences = PreferenceManager.getDefaultSharedPreferences(this)
    }
}