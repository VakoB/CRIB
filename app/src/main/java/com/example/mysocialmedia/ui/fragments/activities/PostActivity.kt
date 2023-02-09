package com.example.mysocialmedia.ui.fragments.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mysocialmedia.R
import com.example.mysocialmedia.adapters.PostAdapter
import com.example.mysocialmedia.databinding.ActivityPostBinding
import com.example.mysocialmedia.firestore.Firestore
import com.example.mysocialmedia.models.Comment
import com.example.mysocialmedia.models.Post
import com.example.mysocialmedia.utils.Constants
import com.example.mysocialmedia.utils.GlideLoader
import java.io.IOException
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.text.DateFormat
import java.util.Calendar

class PostActivity : BaseActivity(), View.OnClickListener {
    private lateinit var postAdapter: PostAdapter
    private var mSelectedPostImageFileUri: Uri? = null
    private var mPostImageURL: String = ""
    private lateinit var binding: ActivityPostBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityPostBinding.inflate(layoutInflater)
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
        binding.linearLayoutId.setOnClickListener(this@PostActivity)
        binding.postSubmit.setOnClickListener(this)



    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.postToolbar)


        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_25)
        }
        binding.postToolbar.setNavigationOnClickListener { onBackPressed() }
    }


    fun postedSuccessfully(postId: String){

        Firestore().getPostDetails(this@PostActivity, postId)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id){

                R.id.linearLayoutId -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.showImageChooserForPosts(this@PostActivity){ imageUri ->
                            binding.imagePost.setImageURI(imageUri)

                        }
                    } else {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
                R.id.postSubmit -> {
                    showProgressDialog("Please Wait...")
                    if (mSelectedPostImageFileUri != null){
                        Firestore().uploadPostImageToCloudStorage(this@PostActivity, mSelectedPostImageFileUri)
                    }
                    else if (mSelectedPostImageFileUri == null && binding.postTitle.text.toString().isNotEmpty()){
                        postWithoutImage()
                    }
                    else{
                        hideProgressDialog()
                        showErrorSnackBar("You haven't made it clear what to post", true)
                    }

                }

            }
        }
    }
    private fun postWithoutImage(){
        var mPostImageURL2 = ""
        val sharedPreferences = getSharedPreferences(
            Constants.MYPREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(
            Constants.LOGGED_IN_USERNAME,"")!!
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        val textOfPost = binding.postTitle.text.toString()
        val time = Calendar.getInstance().time
        val date = DateFormat.getDateInstance().format(time)
        val formattedDate = date
        Log.e("date","date: $formattedDate")
        if (mPostImageURL.isNotEmpty()){
            mPostImageURL2 = mPostImageURL
        }

        val post = Post(
            title = textOfPost,
            image = mPostImageURL2,
            authorId = currentUserID,
            author = username,
            time = formattedDate
        )
        Firestore().setPostsInFirestore(this@PostActivity, post)

    }
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        //uri of selected image from phone storage
                        mSelectedPostImageFileUri = data.data!!

                        //binding.circleImageView.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(mSelectedPostImageFileUri!!,binding.imagePost)
                    } catch (e: IOException){
                        e.printStackTrace()
                        Toast.makeText(this@PostActivity,
                            "Image selection failed.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }
    fun postImageUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mPostImageURL = imageURL
        postWithoutImage()
    }
    fun moveToDashboard(){
        hideProgressDialog()
        Toast.makeText(this@PostActivity, "You have posted successfully.",Toast.LENGTH_LONG).show()
        val intent = Intent(this@PostActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }



}