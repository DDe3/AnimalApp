package com.example.practica.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.io.FileDescriptor


class UriToBitmap {

    fun uriToBitmap(uri: Uri, context: Context): Bitmap {
        val parcelFileDescriptor: ParcelFileDescriptor? = context.contentResolver
            .openFileDescriptor(uri, "r")
        val fileDescriptor: FileDescriptor = parcelFileDescriptor?.fileDescriptor!!
        val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
        parcelFileDescriptor.close()
        return image
    }

    fun bitmapToUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }



}