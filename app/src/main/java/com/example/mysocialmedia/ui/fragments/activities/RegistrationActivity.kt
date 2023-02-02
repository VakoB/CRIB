package com.example.mysocialmedia.ui.fragments.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.mysocialmedia.R
import com.example.mysocialmedia.databinding.ActivityRegistrationBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
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
        binding.register.setOnClickListener { registerUser() }
        binding.regToLogin.setOnClickListener { onBackPressed() }

    }
    private fun setUpActionBar(){

        setSupportActionBar(binding.toolbar)


        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun validateRegisterDetails(): Boolean{
        return  when{
            TextUtils.isEmpty(binding.firstName.text.toString().trim{it <= ' '})
                -> {
                showErrorSnackBar("Please enter First Name", true)
                false
            }
            TextUtils.isEmpty(binding.lastName.text.toString().trim{it <= ' '})
                -> {
                showErrorSnackBar("Please enter Last Name", true)
                false
            }
            TextUtils.isEmpty(binding.etEmailRegister.text.toString().trim{it <= ' '})
                    or (binding.etEmailRegister.text.toString().length > 30)
                    or  (binding.etEmailRegister.text.toString().length <6) -> {
                showErrorSnackBar("Please enter E-mail correctly", true)
                false
            }
            TextUtils.isEmpty(binding.etEmailPassword.text.toString().trim{it <= ' '})
                -> {
                showErrorSnackBar("Please enter your Password correctly", true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPasswordRegister.text.toString().trim{it <= ' '})
                    or  (binding.etConfirmPasswordRegister.text.toString() != binding.etEmailPassword.text.toString()) -> {
                showErrorSnackBar("Please confirm your Password correctly", true)
                false
            }
            !(binding.checkBox.isChecked) ->{
                showErrorSnackBar("Please accept the Terms & Conditions", true)
            false
            }
            else ->{
            true
            }
        }

    }
    private fun registerUser(){
        if (validateRegisterDetails()){
            showProgressDialog("Please Wait...")
            val email = binding.etEmailRegister.text.toString().trim{it <= ' '}
            val password = binding.etEmailPassword.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener (
                    OnCompleteListener<AuthResult> { task ->
                        if (task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                firebaseUser.uid,
                                binding.firstName.text.toString().trim{ it <=' ' },
                                binding.lastName.text.toString().trim{ it <=' ' },
                                binding.etEmailRegister.text.toString().trim{ it <=' ' },

                            )
                            Firestore().registerUser(this@RegistrationActivity,user)
                            //FirebaseAuth.getInstance().signOut()
                            //finish()
                        }
                        else{
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }

                    }
                )
        }
    }
    fun afterRegisteringSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this@RegistrationActivity,
            "You have registered successfully!",
            Toast.LENGTH_SHORT).show()
    }
}