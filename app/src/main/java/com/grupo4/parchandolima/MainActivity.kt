package com.grupo4.parchandolima

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var fbtnRegistrar:TextView
    private lateinit var fbtnVerReporte:TextView
    private lateinit var fbtnVerMapa:TextView


    // VALORES A USAR PARA LAS PRUEBAS
    // emulador pixel 8 api 30 R
    // coordenadas para pruebas
    // -12.103617, -76.963256
    // -12.103783, -76.963510
    //-12.103933, -76.963819

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //cargamos los elementos para reutilizarlos
        fbtnRegistrar = findViewById(R.id.btnReportar)
        fbtnVerReporte = findViewById(R.id.btnVerReporte)
        fbtnVerMapa    = findViewById(R.id.btnVerMapa)

        fbtnRegistrar.setOnClickListener {
        val intent = Intent(this, Reportar1::class.java)
        startActivity(intent)
    }
        fbtnVerReporte.setOnClickListener {
            val intent = Intent(this, ListarActivity::class.java)
            startActivity(intent)
        }

    fbtnVerMapa.setOnClickListener {
        val intent = Intent(this, MapaActivity::class.java)
        startActivity(intent)
    }
}
}