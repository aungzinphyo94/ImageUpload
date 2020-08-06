package com.azp.photoupload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.azp.photoupload.api.ApiClient
import com.azp.photoupload.model.Response
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import java.io.File


class MainActivity : AppCompatActivity() {

    val REQUEST_GALLERY = 100

    val apiClient = ApiClient()

    lateinit var partImage: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pickGallery.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
//            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Open Gallery"), REQUEST_GALLERY)
        }

        btnUpload.setOnClickListener {

            val file: File = File(partImage)
            Log.d("Uri data(path)>>>>>", file.absolutePath)

            val requestBody: RequestBody =
                RequestBody.create(
                    MediaType.parse("multipart/form-data"),
                    file
                )
            val imageFile: MultipartBody.Part =
                MultipartBody.Part.createFormData(
                    "imageUpload",
                    file.name,
                    requestBody
                )

            val apiInterface = apiClient.apiInterface()
            val apiCall = apiInterface.postImage(imageFile)
            apiCall.enqueue(object : Callback<Response> {
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Toast.makeText(
                        applicationContext,
                        t.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    Toast.makeText(
                        applicationContext,
                        response.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

            })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY) {
            val dataImage: Uri? = data!!.data

            Log.d("Uri data>>>>>", dataImage.toString())

            val imageProjection: Array<String> = arrayOf(MediaStore.Images.Media.DATA)

            val cursor: Cursor? = contentResolver.query(
                dataImage!!,
                imageProjection,
                null, null, null
            )

            Log.d("Uri data>>>>>", cursor.toString())

            imageHolder.setImageURI(data.data) // handle chosen image

            cursor!!.moveToFirst()
            val imageIndex: Int = cursor.getColumnIndex(imageProjection[0])
            partImage = cursor.getString(imageIndex)

            Log.d("Uri data>>>>>", partImage.toString())

        }
    }
}