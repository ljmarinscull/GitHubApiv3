package com.example.githubapiv3.domain.models

data class UserDetails(
    val name: String,
    val followers: String,
    val following: String
) {
    companion object {
        fun emptyData() =
            UserDetails("unknown", "unknown", "unknown")
    }
}