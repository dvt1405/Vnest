package com.example.recycleview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    companion object {
        val EXTRA_TEXTNAME: String = MainActivity::class.java.name + ".extra.TEXTNAME"
        val EXTRA_TEXTLAGITUDE = MainActivity::class.java.name + ".extra.TEXTLAGITUDE"
        val EXTRA_TEXTLONGITUDE = MainActivity::class.java.name + ".extra.TEXTLONGITUDE"
        val BASE_URL = "https://www.flickr.com/"
        val API_KEY = "ca370d51a054836007519a00ff4ce59e"
        val METHOD = "flickr.photos.search"
    }

    lateinit var nameText: EditText
    lateinit var lagitudeText: EditText
    lateinit var longitudeText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameText = findViewById(R.id.text_name)
        lagitudeText = findViewById(R.id.text_lagitude)
        longitudeText = findViewById(R.id.text_longitude)
    }

    fun loadImage(view: View) {
        val intent = Intent(this, SecondActivity::class.java)
        intent.apply {
            putExtra(EXTRA_TEXTNAME, nameText.text.toString())
            putExtra(EXTRA_TEXTLAGITUDE, lagitudeText.text.toString())
            putExtra(EXTRA_TEXTLONGITUDE, longitudeText.text.toString())
        }
        startActivity(intent)
    }
}