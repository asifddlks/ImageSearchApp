package com.asifddlks.imagesearchapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageModel(
    val id: String,
    val description: String?,
    val urls: ImageUrls,
    val user: ImageUser
) : Parcelable {

    @Parcelize
    data class ImageUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String
    ) : Parcelable

    @Parcelize
    data class ImageUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=ImageSearchApp&utm_medium=referral"
    }
}