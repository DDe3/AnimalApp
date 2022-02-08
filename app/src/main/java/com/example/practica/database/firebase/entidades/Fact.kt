package com.example.practica.database.firebase.entidades

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Fact (val valor : String? = null) {
}