package com.example.mysocialmedia.ui.fragments.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.mysocialmedia.R
import com.example.mysocialmedia.databinding.ActivityProfileBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.User
import com.example.mysocialmedia.utils.Constants
import com.example.mysocialmedia.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityProfileBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.appColor, null)
        }
        setUpProfileActionBar()
        binding.editProfile.setOnClickListener(this@ProfileActivity)
        binding.logoutProfile.setOnClickListener(this@ProfileActivity)
    }
    private fun setUpProfileActionBar(){

        setSupportActionBar(binding.profileToolbar)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_25)
        }
        binding.profileToolbar.setNavigationOnClickListener { onBackPressed() }
    }
    private fun getUserDetails(){
        showProgressDialog("Please Wait...")
        Firestore().getUserDetails(this@ProfileActivity)
    }
    fun userDetailsSuccess(user: User){
        mUserDetails = user
        hideProgressDialog()
        GlideLoader(this@ProfileActivity).loadUserPicture(user.image, binding.circleImageViewProfile)
        binding.nameProfile.text = "${user.firstname} ${user.lastname}"
        binding.tvGender.text = "${user.gender}"
        binding.tvEmail.text = "${user.email}"


    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null){
            when (v.id){
                R.id.logout_profile -> {
                    val sharedPreferences = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putInt("profileCompleted", 0)
                    editor.apply()
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.editProfile -> {
                    val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_INFO, mUserDetails)
                    startActivity(intent)
                }
            }
        }
    }

}
