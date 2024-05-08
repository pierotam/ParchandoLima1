package com.grupo4.parchandolima

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.grupo4.parchandolima.util.BaseDatos
import com.grupo4.parchandolima.entidades.Reporte
import com.grupo4.parchandolima.modelo.ReporteDAO
import java.util.regex.Pattern


class Reportar1 : AppCompatActivity() {

    /*
    * Plan de actividades:
    * Vincular los clicks a seleccionar_foto1/tomar_foto1 y seleccionar_foto2/tomar_foto2
    * funcion para poder seleccionar foto y obtener la ubicacion de la foto seleccionada (numero_paso)
    * funcion para poder tomar foto y obtener la ubicacion de la foto tomada (numero_paso)
    *
    * Paso1:
    *   Registrar en variables imagen_bache
    * Paso2:
    *   Registrar en variable imagen_detalle
    * Paso3:
    *   Registrar ubicacion y grabar todas las variables en la DB
    * */




        private lateinit var currentPhotoPath: String
        private lateinit var db: BaseDatos

        private lateinit var imagen_bache: String
        private lateinit var imagen_detalle: String
        private lateinit var geocoordenadas: String

        //hacemos referencia a todos los elementos front para su manejo
        private lateinit var ftxtPaso1Resultado: TextView
        private lateinit var fbtnSeleccionarFoto1: TextView
        private lateinit var fbtnTomarFoto1: TextView
        private lateinit var fimgResultado1: ImageView
        private lateinit var ftxtPaso2Resultado: TextView
        private lateinit var fbtnSeleccionarFoto2: TextView
        private lateinit var fbtnTomarFoto2: TextView
        private lateinit var fimgResultado2: ImageView
        private lateinit var ftxtCoordenadas: TextView


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportar1)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = BaseDatos(this)
        // Asignamos los elementos del front a sus variables locales para su manejo
        // Las variables empiezan con f para evitar confusiones
      //  ftxtPaso1Resultado = findViewById(R.id.txtPaso1Result)
        fbtnSeleccionarFoto1 = findViewById(R.id.btnCargarImagen)
       // fbtnTomarFoto1 = findViewById(R.id.btnTomarFoto)
        fimgResultado1 = findViewById(R.id.imgResultado1)

        ftxtPaso2Resultado = findViewById(R.id.txtPaso2Result)
        fbtnSeleccionarFoto2 = findViewById(R.id.btnCargarImagen2)
//        fbtnTomarFoto2 = findViewById(R.id.btnTomarFoto2)
        fimgResultado2 = findViewById(R.id.imgResultado2)

        ftxtCoordenadas = findViewById(R.id.txtCoordenadas)

        //INICIALIZO VARIABLES
        imagen_bache=""
        imagen_detalle=""
        geocoordenadas=""


        // Paso1: Capturamos el evento del botón para seleccionar la foto de la galeria
        // pasamos como atributo el paso para las modificaciones front
        val btnSeleccionarFoto: Button = findViewById(R.id.btnCargarImagen)
        btnSeleccionarFoto.setOnClickListener {
            seleccionarFotoIntent("p1")
        }
        // Paso2: Capturamos el evento del botón para seleccionar la foto de la galeria
        val btnSeleccionarFoto2: Button = findViewById(R.id.btnCargarImagen2)
        btnSeleccionarFoto2.setOnClickListener {
            seleccionarFotoIntent("p2")
        }


        // funcion para seleccionar las fotos tomadas de copilot *

        // Paso1: Capturamos el evento del botón para tomar la foto
        // Retirado en esta versión, falta detalle técnico para tomar la foto y guardar la ruta
//        val btnTomarFoto: Button = findViewById(R.id.btnTomarFoto)
//        btnTomarFoto.setOnClickListener {
//            tomarFotoIntent("p1")
//        }

        // Paso2: Capturamos el evento del botón para tomar la foto
 /*
        val btnTomarFoto2: Button = findViewById(R.id.btnTomarFoto2)
        btnTomarFoto2.setOnClickListener {
            tomarFotoIntent("p2")
        }
*/


        //Paso Final
        val btnRegistrarReporte: Button = findViewById(R.id.btnEnviarReporte)
        btnRegistrarReporte.setOnClickListener{

            geocoordenadas= ftxtCoordenadas.text.toString()
            //validar campos
            var valido=true
            var errores=""

            if(imagen_bache==""){valido=false; errores += " imagen bache no seteada."}

            if(imagen_detalle==""){valido=false; errores = errores + " Imagen detalle no seteada."}


            if (geocoordenadas=="") {valido=false; errores = errores + " Campo coordenadas vacio."
            }else{
                //validamos coordenadas
                val pattern = Pattern.compile("-?[0-9]+(.[0-9]{1,6})?, -?[0-9]+(.[0-9]{1,6})?$")
                val matcher = pattern.matcher(geocoordenadas)
                if (!matcher.matches()){valido=false; errores = errores + " Coordenada debe tener el formato -12.34567, 70.1234156"}
            }



            if(valido){

                //registrar en db
                registrarReporteDB()
            }else{

                mostrarMensaje(errores)



            }
        }

    }


// IMP Paso1: seleccion foto | Codigo base tomado de copilot
private val selectPictureResult1 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == RESULT_OK) {
        //obtenemos la URI
        val selectedImageUri: Uri? = result.data?.data
        //transformamos la uri en el valor que vamos a almacenar. recordar que para convertirlo a uri, usamos variableuri=Uri.parse(variablestring)
        imagen_bache = selectedImageUri.toString()


        //hacemos visible la ruta y deshabilitamos los botones tras seleccionar foto
      //  fbtnTomarFoto1.visibility=View.INVISIBLE
        fbtnSeleccionarFoto1.visibility = View.INVISIBLE
          //asignamos la foto y la hacemos visible
        fimgResultado1.setImageURI(selectedImageUri)
        fimgResultado1.visibility=View.VISIBLE

        //aqui termina porque falta paso 2 antes de guardar en db
    }
}

// seleccion paso 2
    private val selectPictureResult2 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            //obtenemos la URI
            val selectedImageUri2 : Uri? = result.data?.data
            //transformamos la uri en el valor que vamos a almacenar. recordar que para convertirlo a uri, usamos variableuri=Uri.parse(variablestring)
            imagen_detalle = selectedImageUri2.toString()

            //hacemos visible la ruta y deshabilitamos los botones tras seleccionar foto
          //
            //  fbtnTomarFoto2.visibility=View.INVISIBLE
            fbtnSeleccionarFoto2.visibility = View.INVISIBLE
            //asignamos la foto y la hacemos visible
            fimgResultado2.setImageURI(selectedImageUri2)
            fimgResultado2.visibility=View.VISIBLE

            //aqui termina porque falta paso 3... antes de guardar en db.


        }
    }
  /*
    // seleccion paso 3 (pendiente, manejo de mapas y selección de geocoordenadas)
    private val selectPictureResult3 = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {

        }
    }
*/

    private fun seleccionarFotoIntent(paso:String) {
        //No estoy seguro de como trasladar la variable del paso, asi que el filtro lo hacemos en este punto. Refactorizar luego
        if(paso == "p1"){
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { selectPictureIntent ->
                selectPictureResult1.launch(selectPictureIntent)
            }
        }else if(paso =="p2"){
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).also { selectPictureIntent2 ->
                selectPictureResult2.launch(selectPictureIntent2)
            }
        }

}

    @RequiresApi(Build.VERSION_CODES.O)
    fun registrarReporteDB(){
        //ya viene validado

        //preparamos el objeto
            val objetoRegistro = Reporte()
            objetoRegistro.imagenBache=imagen_bache
            objetoRegistro.imagenDetalle=imagen_detalle
            objetoRegistro.geocoordenadas=geocoordenadas

        // registramos el objeto mediante DAO
            val reporteDAO = ReporteDAO(this)
            val resultado = reporteDAO.registrarReporte(objetoRegistro)
            mostrarMensaje(resultado, true)

    }


 /*
 // Codigo para tomar fotos obtenido de copilot. Revisar y adaptar

        private val takePictureResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Save photo location to database
                val photoLocation = FotoUrl(currentPhotoPath, getCurrentLocation())
                db.insertPhotoLocation(photoLocation)
            }
        }
*/
        //funcion para tomar la foto
    /*private fun tomarFotoIntent(paso: String) {
 Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
          // Ensure that there's a camera activity to handle the intent
          takePictureIntent.resolveActivity(packageManager)?.also {
              // Create the File where the photo should go
              val photoFile: File? = try {
                  createImageFile()
              } catch (ex: IOException) {
                  // Error occurred while creating the File
                  null
              }
              // Continue only if the File was successfully created
              photoFile?.also {
                  val photoURI: Uri = FileProvider.getUriForFile(
                      this,
                      "com.grupo4.parchandolima.fileprovider",
                      it
                  )
                  takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                  takePictureResult.launch(takePictureIntent)
              }
          }
      }
        } */

/*
        private fun createImageFile(): File {
           /*
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile(
                "JPEG_${timeStamp}_",
                ".jpg",
                storageDir
            ).apply {
                // Save a file: path for use with ACTION_VIEW intents
                currentPhotoPath = absolutePath
            }
            */
        }
*/
  /*      private fun getCurrentLocation(): Location? {

            return null
        }
*/



    fun  mostrarMensaje(mensaje:String, exito:Boolean=false){
        val ventana = AlertDialog.Builder(this)
        ventana.setTitle("Mensaje Informativo")
        ventana.setMessage(mensaje)
        if(exito){
            ventana.setPositiveButton("Aceptar", DialogInterface.OnClickListener { dialog, which ->
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    })
        }

        ventana.create().show()
    }
}


