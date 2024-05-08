package com.grupo4.parchandolima

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.grupo4.parchandolima.modelo.ReporteDAO

class ListarActivity : AppCompatActivity() {

    private lateinit var rvReportes: RecyclerView
    private val reporteDAO: ReporteDAO = ReporteDAO(this)
    private var adaptador: AdaptadorPersonalizado = AdaptadorPersonalizado()

    private lateinit var fbtnNuevo: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //asignamos elementos front para su uso
        fbtnNuevo = findViewById<FloatingActionButton>(R.id.btnNuevoReporte)


        fbtnNuevo.setOnClickListener {
            val intent = Intent(this,Reportar1::class.java)
            startActivity(intent)
        }
        asignarReferencias()
        mostrarReportes()
    }


    fun mostrarReportes(){
        val listaReportes = reporteDAO.cargarReporte()
        adaptador.agregarDatos(listaReportes)
        adaptador.contexto(this)
    }

    fun asignarReferencias(){


        rvReportes = findViewById(R.id.rvReporte)
        rvReportes.layoutManager = LinearLayoutManager(this)
        rvReportes.adapter = adaptador

        adaptador?.setEliminarItem {
            eliminar(it.id)
        }
    }

    fun eliminar(id:Int){
        val ventana = AlertDialog.Builder(this)
        ventana.setMessage("Desea eliminar la persona?")
        ventana.setCancelable(true)
        ventana.setPositiveButton("SI", DialogInterface.OnClickListener { dialog, which ->
            var mensaje = reporteDAO.eliminarPersona(id)
            mostrarReportes()
            dialog.dismiss()
            mostrarMensaje(mensaje)
        })
        ventana.setNegativeButton("NO", DialogInterface.OnClickListener { dialog, which ->
            dialog.dismiss()
        })
        ventana.create().show()
    }

    fun mostrarMensaje(mensaje:String){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
            val intent = Intent(this,ListarActivity::class.java)
            startActivity(intent)
        })
        ventana.create().show()
    }
}
