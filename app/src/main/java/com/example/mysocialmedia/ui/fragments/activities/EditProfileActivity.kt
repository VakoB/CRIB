package com.example.mysocialmedia.ui.fragments.activities


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.mysocialmedia.databinding.ActivityEditProfileBinding
import com.example.mysocialmedia.models.User
import com.example.mysocialmedia.utils.Constants
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.mysocialmedia.R
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.utils.GlideLoader
import java.io.IOException

class EditProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User
    private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""
    private lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
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



        if (intent.hasExtra(Constants.EXTRA_INFO)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_INFO)!!
        }

        binding.etFirstNameEdit.setText(mUserDetails.firstname)
        binding.etLastNameEdit.setText(mUserDetails.lastname)
        binding.etEmailEdit.isEnabled = false
        binding.etEmailEdit.setText(mUserDetails.email)

        if (mUserDetails.profileCompleted == 0){
            binding.pageTitle.text = resources.getString(R.string.complete_profile)
            binding.etFirstNameEdit.isEnabled = false
            binding.etLastNameEdit.isEnabled = false

        }else{
            setUpActionBar()
            binding.pageTitle.text = resources.getString(R.string.edit_profile)
            GlideLoader(this@EditProfileActivity).loadUserPicture(mUserDetails.image, binding.circleImageView)
            if (mUserDetails.gender == Constants.MALE){
                binding.radioButtonMale.isChecked = true
            }else{
                binding.radioButtonFemale.isChecked = true
            }
        }



        binding.circleImageView.setOnClickListener(this@EditProfileActivity)
        binding.submitEditProfile.setOnClickListener(this@EditProfileActivity)


        }
        private fun setUpActionBar() {

            setSupportActionBar(binding.editProfileToolbar)
            val actionBar = supportActionBar
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true)
                actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_25)
            }
            binding.editProfileToolbar.setNavigationOnClickListener { onBackPressed() }
        }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){

                R.id.circleImageView -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //showErrorSnackBar("You have the storage permission.", false)
                        Constants.showImageChooser(this@EditProfileActivity)

                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.submit_edit_profile -> {
                    showProgressDialog("Please Wait...")
                    if (mSelectedImageFileUri != null){
                        Firestore().uploadImageToCloudStorage(this@EditProfileActivity, mSelectedImageFileUri)
                    }
                    else{
                        updateUserProfileWithoutImage()
                    }

                }
            }
        }
    }
    private fun updateUserProfileWithoutImage(){
        val userHashMap = HashMap<String,Any>()

        val firstName = binding.etFirstNameEdit.text.toString().trim{ it <= ' '}
        if (firstName != mUserDetails.firstname) {
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = binding.etLastNameEdit.text.toString().trim{ it <= ' '}
        if (lastName != mUserDetails.lastname) {
            userHashMap[Constants.FIRST_NAME] = lastName
        }

        val gender = if (binding.radioButtonMale.isChecked){
            Constants.MALE
        }
        else{
            Constants.FEMALE
        }
        if (gender.isNotEmpty() && gender != mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }
        if (mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }

        userHashMap[Constants.GENDER] = gender
        //showProgressDialog("Please Wait...")
        userHashMap[Constants.COMPLETED_PROFILE] = 1 // changing it to one when user logs in the first time, it will remain 1 after the first time



        Firestore().updateUserProfileData(this@EditProfileActivity,userHashMap)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //showErrorSnackBar("The storage permission is granted.", false)
                Constants.showImageChooser(this@EditProfileActivity)
            } else {
                Toast.makeText(this, getString(R.string.read_storage_permission_denied), Toast.LENGTH_LONG).show()
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        //uri of selected image from phone storage
                        mSelectedImageFileUri = data.data!!

                        //binding.circleImageView.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(mSelectedImageFileUri!!,binding.circleImageView)
                    } catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@EditProfileActivity,
                        "Image selection failed.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    fun userProfileUpdateSuccess(){

        Toast.makeText(this@EditProfileActivity,
        "Profile has updated successfully.", Toast.LENGTH_LONG).show()
        val intent = Intent(this@EditProfileActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()

    }
    @SuppressLint("CommitPrefEdits")
    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mUserProfileImageURL = imageURL
        val sharedPreferences =
            getSharedPreferences(
                Constants.MYPREFERENCES,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        if (mSelectedImageFileUri != null){
            editor.putString(Constants.USER_IMG,mUserProfileImageURL)
        }

        updateUserProfileWithoutImage()

    }


}