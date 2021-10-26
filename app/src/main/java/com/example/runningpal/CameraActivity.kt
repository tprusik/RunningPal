package com.example.runningpal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.IOException
import java.net.URI

class CameraActivity : AppCompatActivity() {

    companion object{

        private lateinit var SelectedImageFileUri : Uri
        private const val CAMERA_PERMISSION_CODE = 1
        private const val CAMERA_REQUEST_CODE = 2
        private const val READ_EXTERNAL_STORAGE_PERMISSION_CODE = 121
        private const val READ_EXTERNAL_STORAGE_REQUEST_CODE = 212
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


      btn_CameraShoot.setOnClickListener{
            if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CAMERA
            )== PackageManager.PERMISSION_GRANTED
            ){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)

            }else{
                ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE)
            }


        }

        btn_CameraUpload.setOnClickListener{

            if(SelectedImageFileUri!=null){
               val fileExtension =  MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(SelectedImageFileUri!!))

                val sRef :StorageReference = FirebaseStorage.getInstance().reference.child(
                        "Image "+ System.currentTimeMillis() + "." + fileExtension)

                sRef.putFile(SelectedImageFileUri!!).addOnCompleteListener() {
                    task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result

                        Toast.makeText(this,"Poprawne udostapnienie zdjecia", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this,"Nie Poprawne udostapnienie zdjecia", Toast.LENGTH_LONG).show()

                    }
                }

            }else{
                
                Toast.makeText(this,"Nie ma zdjecia",Toast.LENGTH_SHORT).show()
            }


        }


        btn_CameraChooseFromGallery.setOnClickListener{
            if(ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )== PackageManager.PERMISSION_GRANTED
            ){
                val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST_CODE)

            }else{
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        READ_EXTERNAL_STORAGE_PERMISSION_CODE)
            }


        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode== CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {

                Toast.makeText(this, "Nie przyznano pozwolenia kamery", Toast.LENGTH_SHORT).show()
            }


            if (requestCode == READ_EXTERNAL_STORAGE_PERMISSION_CODE) {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                     startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST_CODE)
                } else {

                    Toast.makeText(this, "Nie przyznano pozwolenia Odczytania zdjęć", Toast.LENGTH_SHORT).show()
                }


            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK){
            if(requestCode== CAMERA_REQUEST_CODE){
                val snapshot : Bitmap = data!!.extras!!.get("data") as Bitmap
                iv_CameraShoot.setImageBitmap(snapshot)
            }

            if(requestCode== READ_EXTERNAL_STORAGE_REQUEST_CODE){
                if(data!=null){
                    try {
                        SelectedImageFileUri =  data.data!!

                        iv_CameraShoot.setImageURI(SelectedImageFileUri)

                    }catch (e: IOException){
                    // coś zrob toast itp

                    }

                }

            }





        }


    }
}