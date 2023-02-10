package com.example.mysocialmedia.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.mysocialmedia.firestore.Firestore
import java.sql.Timestamp

object Constants {
    val POST_ID: String = "postId"
    const val USERS: String = "users"
    const val MYPREFERENCES: String = "MyPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_INFO: String = "extra_user_details"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val FIRST_NAME = "firstname"
    const val LAST_NAME = "lastname"
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val IMAGE: String = "image"
    const val COMPLETED_PROFILE = "profileCompleted"
    const val POSTS: String = "posts"
    const val POST_IMAGE2 = "Post_Image"
    const val POST_IMAGE = "image"
    const val POST_TITLE = "title"
    const val TIME = "time"
    const val AUTHOR_ID = "authorId"
    const val AUTHOR_NAME = "author"
    const val LIKES = "likes"
    const val COMMENTS = "comments"
    const val USER_ID = "uid"
    const val USER_IMG = "userImage"


    fun showImageChooser(activity: Activity){
        //intent for launching an image selection of phone storage
        val galleryIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //launches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        //MimeTypeMap: Two-way map that maps MIME_types to file extensions and vice versa.
        //getSingleaton(): Get singleton instance of Mimetypemap
        //getExtensionFromMimeType: return the registered extension fro the given MIME type
        //contentRsolver.getType: return the MIME type of the given content URL
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
    fun showImageChooserForPosts(activity: Activity, callback: ((Uri?) -> Unit)? = null) {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
        callback?.invoke(galleryIntent.data)
    }

}