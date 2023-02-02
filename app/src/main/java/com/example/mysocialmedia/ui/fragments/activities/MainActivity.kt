package com.example.mysocialmedia.ui.fragments.activities

import android.content.Context
import android.os.Bundle
import com.example.mysocialmedia.databinding.ActivityMainBinding
import com.example.mysocialmedia.utils.Constants

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