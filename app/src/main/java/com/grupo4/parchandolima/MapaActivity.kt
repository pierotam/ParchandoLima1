package com.grupo4.parchandolima

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.grupo4.parchandolima.modelo.ReporteDAO
import java.util.regex.Pattern

class MapaActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var map:GoogleMap
    private val reporteDAO: ReporteDAO = ReporteDAO(this)

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mapa)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mapa)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //referenciamos
        asignarReferencias()


    }

    fun asignarReferencias(){
        val mapFragment:SupportMapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap){
        map = p0
        map.uiSettings.isZoomControlsEnabled=true

        //¿Como pasamos parametros de un intent a otro?
        // Aquí hacer un recorrido de todas los reportes, por cada uno extraer la coordenada
        // convertir la coordenada en lat y long. Validar formato, ignorar is no tiene el formato correcto
        //agregar marcador

        val pattern = Pattern.compile("-?[0-9]+(.[0-9]{1,6})?, -?[0-9]+(.[0-9]{1,6})?$")
        val listaReportes = reporteDAO.cargarReporte()
        var flagprimeracoordenada = false
        var zoominicial = LatLng(-12.103617, -76.963256) //upc por omisión
        for (rep in listaReportes ){
            val coordenada = rep.geocoordenadas
            val matcher = pattern.matcher(coordenada)
            //solo agregamos si el formato corresponde con el de una coordenada
            if (matcher.matches()) {
                val arrCoordenadas = coordenada.split(",")
                val coordenadaBache = LatLng(arrCoordenadas[0].toDouble(), arrCoordenadas[1].toDouble())
                val marcador = MarkerOptions().position(coordenadaBache).title(rep.id.toString())
                map.addMarker(marcador)
                if(!flagprimeracoordenada){
                    flagprimeracoordenada=true
                    zoominicial = coordenadaBache
                }
            }
        }

map.animateCamera(CameraUpdateFactory.newLatLngZoom(zoominicial, 18f))


    }
}