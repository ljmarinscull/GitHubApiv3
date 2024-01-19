package com.example.githubapiv3.data.models

data class UserJO(
    val login: String,
    val url: String,
    val avatar_url: String,
    val followers_url: String,
    val following_url: String,
    val repos_url: String
)