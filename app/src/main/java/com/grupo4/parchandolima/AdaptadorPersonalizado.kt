package com.grupo4.parchandolima

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.grupo4.parchandolima.entidades.Reporte

class AdaptadorPersonalizado:RecyclerView.Adapter<AdaptadorPersonalizado.MiViewHolder>() {

    private var listaReportes:ArrayList<Reporte> = ArrayList()
    private lateinit var context: Context
    private var eliminarItem:((Reporte) -> Unit)? = null

    fun agregarDatos(item: ArrayList<Reporte>){
        this.listaReportes = item
    }
    fun contexto(context: Context){
        this.context = context
    }
    fun setEliminarItem(callback:(Reporte) ->Unit){
        this.eliminarItem = callback
    }

    class MiViewHolder(var view: View):RecyclerView.ViewHolder(view) {
        //TODO cambiar
        private var fImagenBache = view.findViewById<ImageView>(R.id.imgBache)
        private var fFecha = view.findViewById<TextView>(R.id.filaFecha)
        private var fEstado = view.findViewById<TextView>(R.id.filaEstado)
        private var fGeocoordenadas = view.findViewById<TextView>(R.id.filaCoordenadas)


       // var filaEditar = view.findViewById<ImageButton>(R.id.filaEditar)
       // var filaEliminar = view.findViewById<ImageButton>(R.id.filaEliminar)

        fun bindView(reporte: Reporte){
            //convertir nuevamente a URI
            var variableuri= Uri.parse(reporte.imagenBache)
            fImagenBache.setImageURI(variableuri)

            fGeocoordenadas.text = reporte.geocoordenadas
            fFecha.text = reporte.fecha
            fEstado.text = reporte.estado
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MiViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.fila,parent,false)
    )

    override fun onBindViewHolder(holder: AdaptadorPersonalizado.MiViewHolder, position: Int) {
        val reporteItem = listaReportes[position]
        holder.bindView(reporteItem)

       /*
        holder.filaEditar.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra("var_id", listaReportes[position].id)
            intent.putExtra("var_dni", listaReportes[position].dni)
            intent.putExtra("var_nombres", listaReportes[position].nombres)
            intent.putExtra("var_correo", listaReportes[position].correo)
            context.startActivity(intent)
        }

        holder.filaEliminar.setOnClickListener {
            eliminarItem?.invoke(personaItem)
        }

        */
    }

    override fun getItemCount(): Int {
        return listaReportes.size
    }
}