package com.example.mysocialmedia

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.mysocialmedia.databinding.ActivityLoginBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.User
import com.example.mysocialmedia.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.loginToRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
        binding.logIn.setOnClickListener { loginUser() }
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPassActivity::class.java)
            startActivity(intent)
        }

    }
    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(binding.etEmailLogin.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar("Please enter E-mail",true)
                false
            }
            TextUtils.isEmpty(binding.etPasswordLogin.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar("Please enter Password",true)
                false
            }
            else ->{
                true
            }
        }
    }
    private fun loginUser(){
        if (validateLoginDetails()){
            showProgressDialog("Please Wait...")
            val email = binding.etEmailLogin.text.toString().trim{it <= ' '}
            val password = binding.etPasswordLogin.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        Firestore().getUserDetails(this@LoginActivity)
                    }
                    else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }



        }


    }
    fun userLoggedInSuccess(user: User){
        hideProgressDialog()


        if (user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, EditProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_INFO, user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        }
        finish()
    }

}