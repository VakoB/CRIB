package com.example.mysocialmedia.ui.fragments.activities

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.mysocialmedia.R
import com.example.mysocialmedia.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotPassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        setUpActionBar()
    }
    private fun setUpActionBar(){

        setSupportActionBar(binding.forgotPassToolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_25)
        }
        binding.forgotPassToolbar.setNavigationOnClickListener { onBackPressed() }
        binding.submit.setOnClickListener {
            val email = binding.etEmailForgotPass.text.toString().trim { it <= ' '}
            if (email.isEmpty()){
                showErrorSnackBar("Enter your E-mail", true)
            }
            else{
                showProgressDialog("Sending...")
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            hideProgressDialog()
                            Toast.makeText(this@ForgotPassActivity,"Email sent successfully",Toast.LENGTH_LONG).show()
                            finish()
                        }
                        else{
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }

                    }
            }

        }
    }
}