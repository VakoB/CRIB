package com.example.mysocialmedia.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Adapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mysocialmedia.adapters.PostAdapter
import com.example.mysocialmedia.models.Post
import com.example.mysocialmedia.models.User
import com.example.mysocialmedia.ui.fragments.activities.*
import com.example.mysocialmedia.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Firestore {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegistrationActivity, userInfo: User) {

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.afterRegisteringSuccessfully()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering a user",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){

        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYPREFERENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key: logged_in_username
                //value: "firstname lastname"
                editor.putString(Constants.LOGGED_IN_USERNAME,
                    "${user.firstname} ${user.lastname}"
                )
                editor.apply()



                when (activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is ProfileActivity -> {
                        activity.userDetailsSuccess(user)
                    }


                }

            }
            .addOnFailureListener { e ->
                when (activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is ProfileActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e(activity.javaClass.simpleName,"Error while logging in",e )


            }




    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity){
                    is EditProfileActivity -> {
                        activity.userProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener { e ->
                when (activity){
                    is EditProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                "Error while updating your user details.",e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance()
            .reference.child(Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
            + Constants.getFileExtension(activity, imageFileURI))
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                //get the downloadable url from the task snapshot

                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when (activity){
                            is EditProfileActivity -> {
                                activity.imageUploadSuccess(uri.toString())
                            }
                        }
                    }

            }
            .addOnFailureListener{ exception ->
                when (activity){
                    is EditProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )

            }

    }
    fun setPostsInFirestore(activity: PostActivity, postInfo: Post){
        val postId = postIdGenerator()
        postInfo.id = postId
        mFirestore.collection(Constants.POSTS)
            .document(postId)
            .set(postInfo)
            .addOnSuccessListener {

                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.MYPREFERENCES,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString(Constants.POST_ID,
                    postId
                )
                editor.apply()

                activity.postedSuccessfully(postId)

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                activity.showErrorSnackBar("Error while uploading a post", true)
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading a post.",
                    e
                )
            }
    }
    fun uploadPostImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance()
            .reference.child(Constants.POST_IMAGE2 + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileURI))
        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URL", taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                //get the downloadable url from the task snapshot

                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        when (activity){
                            is PostActivity -> {
                                activity.postImageUploadSuccess(uri.toString())
                            }
                        }
                    }

            }
            .addOnFailureListener{ exception ->
                when (activity){
                    is PostActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )

            }

    }
    fun setPostInFirestore(activity: Activity, postHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.POSTS)
            .document("${getCurrentUserID()}_post${System.currentTimeMillis()}")
            .set(postHashMap, SetOptions.merge())
            .addOnSuccessListener {

            }
            .addOnFailureListener { e ->

            }
    }
    private fun postIdGenerator(): String{
        val userId = Firestore().getCurrentUserID()
        val postId = "${userId}_post${System.currentTimeMillis()}"
        return  postId
    }
    fun getPostDetails(activity: Activity, postId: String, postAdapter: PostAdapter){

        mFirestore.collection(Constants.POSTS)
            .document(postId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                var post = document.toObject(Post::class.java)!!
                postAdapter.updatePost(post)
                when (activity){
                    is PostActivity -> {
                        activity.moveToDashboard()
                    }
                }



            }
            .addOnFailureListener { e ->
                when (activity){
                    is PostActivity -> {
                        activity.hideProgressDialog()
                    }

                }
                Log.e(activity.javaClass.simpleName,"Error while logging in",e )


            }




    }
}

