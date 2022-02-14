package com.example.practica.logica

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.example.practica.util.Connection
import com.example.practica.util.DateUtils
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata

class FirebaseUpload {

    private val directorio = "images"
    private val ref = FirebaseStorage.getInstance().reference

    fun uploadAvistamiento(name: String, uri: Uri, context: Context) {

        if (Connection().isOnline(context)) {
            val metadata = storageMetadata {
                setCustomMetadata("tipo", name)
            }
            val fileName = DateUtils().getFechayHora()
            val upload = ref.child("$directorio/$fileName")
            upload.putFile(uri, metadata).addOnSuccessListener {
                Toast.makeText(context, "Gracias por ayudarnos a mejorar la aplicación!", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }.addOnCanceledListener {
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Sin conexión a Internet", Toast.LENGTH_LONG).show()
        }


    }


}