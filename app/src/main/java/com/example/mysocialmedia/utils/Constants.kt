package com.example.mysocialmedia.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val USERS: String = "users"
    const val MYPREFERENCES: String = "MyPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_INFO: String = "extra_user_details"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val IMAGE: String = "image"
    const val COMPLETED_PROFILE = "profileCompleted"

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
}