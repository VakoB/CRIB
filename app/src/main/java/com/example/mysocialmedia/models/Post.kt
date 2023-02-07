package com.example.mysocialmedia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.firebase.Timestamp


@Parcelize
class Post(
    val title: String = "",
    val image: String = "",
    val time: Timestamp = Timestamp.now(),
    val authorId: String = "",
    val author: String = "",
    val likes: Long = 0,
    val comments: List<Comment> = emptyList(),
    var id: String = "",
) : Parcelable
