package com.example.imagecapture_savinginsharedstorage

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    var writeExternalStoragePermission = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        writeExternalStorage

        camera.setOnClickListener {
            if (writeExternalStoragePermission) {

                Intent().also {
                    it.action = MediaStore.ACTION_IMAGE_CAPTURE
                    startActivityForResult(it, 1)
                }
            }
        }


    }



    val writeExternalStorage: Unit
        get() {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeExternalStoragePermission = true
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream())
            var path = MediaStore.Images.Media.insertImage(this.contentResolver, bitmap, DateFormat.getDateTimeInstance().format(Date()), "")

            var uri = Uri.parse(path)
            iv_camera.setImageURI(uri)

        }
    }
}
