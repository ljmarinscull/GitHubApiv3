package com.example.githubapiv3.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val username: String,
    val details: String,
    val avatar: String,
    val followers: String,
    val following: String,
    val repos : String
): Parcelable