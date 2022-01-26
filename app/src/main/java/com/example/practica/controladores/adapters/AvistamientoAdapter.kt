package com.example.practica.controladores.adapters

import android.app.AlertDialog
import android.text.Layout
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterResults
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.practica.entidades.Avistamiento
import com.example.practica.R
import com.example.practica.databinding.ItemAvistamientoListBinding
import com.squareup.picasso.Picasso

class AvistamientoAdapter(lista: MutableList<Avistamiento>) :
    RecyclerView.Adapter<AvistamientoAdapter.AvistamientoViewHolder>() {

    private var avistamientoList: MutableList<Avistamiento> = lista


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvistamientoViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_avistamiento_list, parent, false)
        return AvistamientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvistamientoViewHolder, position: Int) {
        val item = avistamientoList[position]
        holder.render(item, position)
    }

    override fun getItemCount(): Int = avistamientoList.size

    fun filterAvistamientos() {

    }





    inner class AvistamientoViewHolder(avistamientoView: View) :
        RecyclerView.ViewHolder(avistamientoView) {

        var binding = ItemAvistamientoListBinding.bind(avistamientoView)

        fun render(item: Avistamiento, index: Int) {
            binding.txtTitulo.text = item.nombre
            binding.txtFecha.text = item.fecha.toString()

            // TODO cambiar la imagen por la URI del Avistamiento
            Picasso.get().load(R.drawable.gato_claro).fit().into(binding.imgAvistamiento)

            binding.btnBorrarAvistamiento.setOnClickListener {
                deleteItem(index)
            }

        }

        fun deleteItem(index: Int) {

            val title = SpannableString("Confirmación borrar elemento")

            title.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                title.length,
                0
            )
            val dialogo = SpannableString("¿Estás seguro que quieres eliminar este avistamiento permanentemente?")
            dialogo.setSpan(
                    AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                    0,
                    dialogo.length,
                    0
                )
            val builder = AlertDialog.Builder(itemView.context)
            builder.setTitle(title)
                .setMessage(dialogo)
                .setCancelable(false)
                .setPositiveButton("Si") { dialog, id ->
                    // TODO acuerdate de mandar a borrar a la DB en AvistamientoUso de aqui
                    avistamientoList.removeAt(index)
                    notifyItemRemoved(index)
                    notifyItemRangeChanged(0, itemCount)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
                .setIcon(R.drawable.ic_borrar_para_siempre)
            val alert = builder.create()
            alert.show()


        }


    }



}

