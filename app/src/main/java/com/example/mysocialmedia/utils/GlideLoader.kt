package com.example.mysocialmedia.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.mysocialmedia.R
import java.io.IOException
import java.net.URI

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView) {             //was imageURI: Url
        try {
            //load user image in imageview
            Glide
                .with(context)
                .load(image) //URI of the image
                .centerCrop() //scale type of image
                .placeholder(R.drawable.ic_baseline_person_24) //default image if it fails to load
                .into(imageView) //the view that in image view will be loaded
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

}