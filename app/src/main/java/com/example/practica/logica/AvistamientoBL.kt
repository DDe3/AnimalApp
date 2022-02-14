package com.example.practica.logica

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.practica.casoUso.AvistamientoUso
import com.example.practica.database.entidades.Avistamiento
import com.example.practica.util.DateUtils
import com.example.practica.util.UriToBitmap
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AvistamientoBL {

    val directorio = "images"  // DIRECTORIO DE IMAGENES

    suspend fun getListaAvistamientos() : MutableList<Avistamiento> {
        return AvistamientoUso().getAllAvistamientos().toMutableList()
    }

    suspend fun saveAvistamiento(context : Context, nombre : String, uri: Uri) {
        try {
            val fecha = DateUtils().getFecha()
            val fileName = UUID.randomUUID().toString()
            val avistamiento = Avistamiento(nombre = nombre.uppercase(Locale.getDefault()), fecha = fecha, fileName = fileName)
            val bitmap = UriToBitmap().uriToBitmap(uri,context)


            AvistamientoUso().saveAvistamiento(avistamiento)

            ImageSaver(context)
                .setFileName("$fileName.png")
                .setDirectoryName(directorio)
                .save(bitmap)

            Log.d("SAVE:", "Me mandaste $nombre, y lo llamaste $fileName en $fecha")
        } catch (e: Exception) {
            Log.d("SAVE:", "No se pudo guardar el avistamiento en bitacora porque ${e.toString()}")
        }

    }

    suspend fun deleteAvistamiento(avistamiento: Avistamiento) {
        AvistamientoUso().deleteAvistamiento(avistamiento)
    }


}