package com.example.taller2

import android.content.pm.PackageManager
import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.taller2.databinding.ActivityImageUploadBinding
import java.io.FileNotFoundException
import java.io.InputStream

class ImageUploadActivity : AppCompatActivity() {

    companion object {
        private const val GALLERY_REQUEST_CODE = 1
        private const val LOAD_REQUEST_CODE = 2
        private const val CAMERA_REQUEST_CODE = 3
        private const val CAPTURE_REQUEST_CODE = 4
    }

    //private val cameraPermission = registerForActivityResult(ActivityResultContracts.TakePicture()){
        //if(it){
            //setImage(uriCamera)
        //}
    //}
    //private lateinit var uriCamera: Uri

    private lateinit var binding: ActivityImageUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()

        //

    }
    private fun setListeners(){
        binding.selectImageButton.setOnClickListener {
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                //No tiene el permiso, pedirlo
                //ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), GALLERY_REQUEST_CODE)
            }else{
                //Ya tiene el permiso
                openGallery()
            }
        }
        binding.cameraButton.setOnClickListener {
            //val file = File(filesDir, "picFromCamera")
            //uriCamera = FileProvider.getUriForFile(baseContext, applicationContext.packageName+".fileprovider", file)
            //cameraPermission.launch(uriCamera)
            val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                //No tiene el permiso, pedirlo
                //ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }else{
                //Ya tiene el permiso
                takePicture()
            }
        }
    }


    //private fun setImage(uri: Uri){
        //binding.imageImageview.setImageURI(uri)
    //}

    fun takePicture(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            startActivityForResult(takePictureIntent, CAPTURE_REQUEST_CODE)
        }catch (e: ActivityNotFoundException){
            e.message?.let { Log.e("PERMISSION_APP", it)}
        }
    }

    fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, LOAD_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            GALLERY_REQUEST_CODE -> {
                //If request is cancelled, the result arrays are empty
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //Permiso concedido
                    openGallery()
                }else{
                    //Permiso denegado
                    //Funciones limitadas
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return
            }
            CAMERA_REQUEST_CODE -> {
                //If request is cancelled, the result arrays are empty
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    //Permiso concedido
                    takePicture()
                }else{
                    //Permiso denegado
                    //Funciones limitadas
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            LOAD_REQUEST_CODE -> {
                if(resultCode == Activity.RESULT_OK){
                    try{
                        val imageUri: Uri? = data?.data
                        val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                        val selectedImage = BitmapFactory.decodeStream(imageStream)
                        binding.imageImageview.setImageBitmap(selectedImage)
                        saveImage(selectedImage)
                    }catch (e: FileNotFoundException){
                        e.printStackTrace()
                    }
                }
            }
            CAPTURE_REQUEST_CODE -> {
                if(resultCode == RESULT_OK){
                    val extras: Bundle? = data?.extras
                    val imageBitmap = extras?.get("data") as? Bitmap
                    binding.imageImageview.setImageBitmap(imageBitmap)
                    saveImage(imageBitmap!!)
                }
            }
        }
    }

    private fun saveImage(image: Bitmap) {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            contentResolver,
            image,
            "title",
            "description"
        )
        Toast.makeText(this, "Imagen guardada en la galería", Toast.LENGTH_SHORT).show()
    }


}