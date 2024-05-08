package com.grupo4.parchandolima.modelo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.grupo4.parchandolima.entidades.Reporte
import com.grupo4.parchandolima.util.BaseDatos
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ReporteDAO(context: Context) {

    private var base:BaseDatos = BaseDatos(context)

    @RequiresApi(Build.VERSION_CODES.O)
    fun registrarReporte(reporte: Reporte):String{
        Log.println(android.util.Log.INFO, "RPV", "INICIANDO registrarReporte")

        var respuesta = ""
        var db = base.writableDatabase

        /* INICIO funcion obtenida de gemini */
        val currentDate = LocalDate.now()
        val formattedDate = currentDate.format(DateTimeFormatter.ISO_DATE)
        /* FIN  */
        Log.println(android.util.Log.INFO, "RPV", "paso 2")

        try {
            Log.println(android.util.Log.INFO, "RPV", "INICIANDO try")

            val valores = ContentValues()
            valores.put("imagen_bache", reporte.imagenBache )
            valores.put("imagen_detalle", reporte.imagenDetalle)
            valores.put("coordenadas", reporte.geocoordenadas)
            valores.put("fecha_actual", formattedDate )
            valores.put("estado", "pendiente")
            Log.println(android.util.Log.INFO, "RPV", "paso 3")

            // Insertamos los datos en la tabla reporte y retornamos la respuesta
            val r = db.insert("reporte", null, valores )
            Log.println(android.util.Log.INFO, "RPV", "paso 4")

            if(r== -1L){

                respuesta = "respuesta no exitosa"
            }else {

                respuesta = "respuesta exitosa"
            }

        }catch(e:Exception){
            Log.println(android.util.Log.INFO, "RPV", "exce")

            respuesta = e.message.toString()
        }

        return respuesta

    }




/*
    fun registrarPersona(reporte: Reporte):String{
        var respuesta = ""
        val db = base.writableDatabase
        try {
            val valores = ContentValues()
            valores.put("dni", reporte.dni)
            valores.put("nombres", reporte.nombres)
            valores.put("correo", reporte.correo)

            val r = db.insert("personas",null,valores)
            if(r == -1L){
                respuesta = "Ocurrio un error al insertar"
            }else{
                respuesta = "Se registró correctamente"
            }
        }catch (e:Exception){
            respuesta = e.message.toString()
        }finally {
            db.close()
        }

        return respuesta
    }
*/
/*    fun modificarPersona(persona: Persona):String{
        var respuesta = ""
        val db = base.writableDatabase
        try {
            val valores = ContentValues()
            valores.put("dni", persona.dni)
            valores.put("nombres", persona.nombres)
            valores.put("correo", persona.correo)

            val r = db.update("personas",valores,"id="+persona.id,null)
            if(r == -1){
                respuesta = "Ocurrio un error al actualizar"
            }else{
                respuesta = "Se actualizó correctamente"
            }
        }catch (e:Exception){
            respuesta = e.message.toString()
        }finally {
            db.close()
        }

        return respuesta
    }
*/
    fun cargarReporte():ArrayList<Reporte>{
        val listarReportes:ArrayList<Reporte> = ArrayList()
        val query = "SELECT * FROM reporte"
        val db = base.readableDatabase
        val cursor: Cursor

        try {
            cursor = db.rawQuery(query,null)
            if(cursor.count > 0){
                cursor.moveToFirst()
                do {
                    val reporte = Reporte()
                    reporte.id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))

                    reporte.imagenBache = cursor.getString(cursor.getColumnIndexOrThrow("imagen_bache"))
                    reporte.imagenDetalle = cursor.getString(cursor.getColumnIndexOrThrow("imagen_detalle"))
                    reporte.geocoordenadas = cursor.getString(cursor.getColumnIndexOrThrow("coordenadas"))
                    reporte.fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha_actual"))
                    reporte.estado = cursor.getString(cursor.getColumnIndexOrThrow("estado"))
                    listarReportes.add(reporte)
                }while (cursor.moveToNext())
            }
        }catch (e:Exception){
            Log.d("===",e.message.toString())
        }finally {
            db.close()
        }

        return listarReportes
    }

    fun eliminarPersona(id:Int):String{
        var respuesta = ""
        val db = base.writableDatabase
        try {
            var r = db.delete("reporte","id="+id,null)
            if(r == -1){
                respuesta = "Error al eliminar"
            }else{
                respuesta = "Se eliminó correctamente"
            }
        }catch (e:Exception){
            Log.d("===",e.message.toString())
        }finally {
            db.close()
        }
        return respuesta
    }
}