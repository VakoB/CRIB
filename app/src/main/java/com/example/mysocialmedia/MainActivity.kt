package com.example.mysocialmedia

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.mysocialmedia.databinding.ActivityMainBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.User
import com.example.mysocialmedia.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences(
            Constants.MYPREFERENCES,Context.MODE_PRIVATE)
        val firstNlastName = sharedPreferences.getString(
            Constants.LOGGED_IN_USERNAME,"")!!
        binding.textboy.text = "Hello $firstNlastName"

    }
}