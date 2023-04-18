package com.example.taller2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.taller2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
    }
    private fun setListeners(){
        binding.imageImagebutton.setOnClickListener {
            val int = Intent(baseContext, ImageUploadActivity::class.java)
            startActivity(int)
        }
        binding.mapsImagebutton.setOnClickListener {
            val int = Intent(baseContext, MapActivity::class.java)
            startActivity(int)
        }
        binding.contactsImagebutton.setOnClickListener{
            val int = Intent(baseContext, ContactsActivity::class.java)
            startActivity(int)
        }
    }
}