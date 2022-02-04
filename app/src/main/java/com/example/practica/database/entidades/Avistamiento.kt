package com.example.practica.database.entidades

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Avistamiento (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre : String,
    val fecha : String,
    val fileName : String)