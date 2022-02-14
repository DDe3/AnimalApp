package com.example.practica.util

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    fun getFecha(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

    fun getFechayHora(): String {
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        return formatter.format(Date())
    }
}