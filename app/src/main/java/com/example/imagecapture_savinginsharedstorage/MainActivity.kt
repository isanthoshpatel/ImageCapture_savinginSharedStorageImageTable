package com.example.imagecapture_savinginsharedstorage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var newFile:File? = null
    var writeExternalStoragePermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        writeExternalStorage

        camera.setOnClickListener {
            if (writeExternalStoragePermission) {

                Intent().also { i ->
                    i.action = MediaStore.ACTION_IMAGE_CAPTURE

                    startActivityForResult(i,1)
                }
            }

        }

    }

    val writeExternalStorage: Unit
        get() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                writeExternalStoragePermission = true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.size >= 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                writeExternalStoragePermission = true
            } else {
                writeExternalStorage
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            var bitmap = data!!.extras!!.get("data") as Bitmap


            //var file = File(getFilesDir,filename)
            //var file = File.createTempFile("pre","suf",catchDir)
            //var file = File(getExternalFilesDir(Environment.DIRECTORY_DCIM),filename)
            //var file = File(getExternalCatchDir(Environment.DIRECTORY_DCIM),filename)
            var file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"JPEG"+DateFormat.getDateTimeInstance().format(Date()))
            //all above are, app specific, so encripted not useful for user to view

            var outputstream = FileOutputStream(file)

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputstream)
           //var path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, DateFormat.getDateTimeInstance().format(Date()), "")
           //this is SharedMediaStore, where data is available even data related app is uninstalled


            var uri = Uri.parse(file.path)
            iv_camera.setImageURI(uri)

        }
    }


}
