package com.example.mysocialmedia.ui.fragments.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mysocialmedia.R
import com.example.mysocialmedia.databinding.ActivityBaseBinding
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBaseBinding
    private lateinit var mProgressDialog: Dialog
    private var doubleBackToExitOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBaseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content),
        message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view


        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.Error
                )
            )
        }
        else{

            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this, R.color.Success
                )
            )

        }
        snackBar.show()
    }
    fun showProgressDialog(text: String){
        mProgressDialog = Dialog(this)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<TextView>(R.id.progressText).text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
    fun doubleBackToExit(){
        if (doubleBackToExitOnce){
            super.onBackPressed()
        }
        this.doubleBackToExitOnce = true

        Toast.makeText(this@BaseActivity, "Press the back button again to exit.", Toast.LENGTH_SHORT).show()
        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitOnce = false}, 2000)
    }
}