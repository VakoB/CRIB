package com.example.mysocialmedia.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mysocialmedia.R
import java.io.IOException
import java.net.URI

class GlideLoader(val context: Context) {

    fun loadUserPicture(imageURI: Uri, imageView: ImageView) {
        try {
            //load user image in imageview
            Glide
                .with(context)
                .load(imageURI) //URI of the image
                .centerCrop() //scale type of image
                .placeholder(R.drawable.ic_launcher_foreground) //default image if it fails to load
                .into(imageView) //the view that in image view will be loaded
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}