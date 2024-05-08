package com.grupo4.parchandolima.util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BaseDatos(context:Context):SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION){
    companion object{
        private const val DATABASE_NAME="dbparchandolima.db"
        private const val VERSION= 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
       var sql ="CREATE TABLE IF NOT EXISTS reporte " +
        "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "imagen_bache TEXT , " +
        "imagen_detalle TEXT , " +
        "coordenadas  TEXT , "+
        "fecha_actual  TEXT , "+
        "estado TEXT);"

        if(db != null) {

            db.execSQL(sql)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS reporte")
        onCreate(db)

    }


}