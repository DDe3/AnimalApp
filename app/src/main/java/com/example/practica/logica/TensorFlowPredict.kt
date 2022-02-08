package com.example.practica.logica


import android.content.Context
import android.net.Uri

import android.graphics.Bitmap

import com.example.practica.ml.MobilenetV110224Quant
import com.example.practica.util.UriToBitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import kotlin.math.roundToLong


class TensorFlowPredict {

    private val fileName = "labels.txt"
    private lateinit var townList: List<String>

    companion object {
        private var minimo: Float? = null

        fun getMinimo(): String {
            return ((minimo!! / 1000) * 100).roundToLong().toString() + "%"
        }
    }


    fun predecirImagen(uri: Uri, context: Context): String {

        leerLabel(context)

        val image = UriToBitmap().uriToBitmap(uri,context)


        val resized: Bitmap = Bitmap.createScaledBitmap(image, 224, 224, true)
        val model = MobilenetV110224Quant.newInstance(context)

        // Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.UINT8)

        val tBuffer = TensorImage.fromBitmap(resized)
        val byteBuffer = tBuffer.buffer

        inputFeature0.loadBuffer(byteBuffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature0.floatArray)

        // Releases model resources if no longer used.
        model.close()

        return townList[max]

    }

    fun getPorcentaje() : String {
        //
        return getMinimo()
    }

    private fun getMax(arr: FloatArray): Int {
        var index = 0
        var min = 0.0f
        for (i in arr.indices) {
            if (arr[i] > min) {
                index = i
                min = arr[i]
            }
        }
        minimo = min
        return index
    }

    private fun leerLabel(context: Context) {
        val inputString =
            context.applicationContext.assets.open(fileName).bufferedReader().use { it.readText() }
        townList = inputString.split("\n")
    }


}