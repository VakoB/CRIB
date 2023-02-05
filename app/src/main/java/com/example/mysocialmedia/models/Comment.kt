package com.example.mysocialmedia.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import com.google.firebase.Timestamp


@Parcelize
class Comment(
    val text: String = "",
    val author: String = "",
    val time: Timestamp = Timestamp.now()
) : Parcelable