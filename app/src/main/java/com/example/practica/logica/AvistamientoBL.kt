package com.example.practica.logica

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.practica.casoUso.AvistamientoUso
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.presentacion.ResultActivity
import com.example.practica.util.UriToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AvistamientoBL {

    val directorio = "images"

    suspend fun getListaAvistamientos() : MutableList<Avistamiento> {

        //delay(5000)
        return AvistamientoUso().getAllAvistamientos().toMutableList()

    }

    suspend fun saveAvistamiento(context : Context, nombre : String, uri: Uri) {
        try {
            val fecha = getFecha()
            val fileName = UUID.randomUUID().toString()
            val avistamiento = Avistamiento(nombre = nombre.uppercase(Locale.getDefault()), fecha = fecha, fileName = fileName)
            val bitmap = UriToBitmap().uriToBitmap(uri,context)


            AvistamientoUso().saveAvistamiento(avistamiento)

            ImageSaver(context)
                .setFileName("$fileName.png")
                .setDirectoryName(directorio)
                .save(bitmap)

            Log.d("SAVE:", "Me mandaste $nombre, y lo llamaste $fileName en ${getFecha()}")
        } catch (e: Exception) {
            Log.d("SAVE:", "No se pudo guardar el avistamiento en bitacora porque ${e.toString()}")
        }

    }

    suspend fun deleteAvistamiento(avistamiento: Avistamiento) {
        AvistamientoUso().deleteAvistamiento(avistamiento)
    }


    private fun getFecha(): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    }

}