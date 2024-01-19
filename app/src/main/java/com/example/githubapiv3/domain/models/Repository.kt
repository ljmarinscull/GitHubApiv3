package com.example.githubapiv3.domain.models

data class Repository(
    val id: Int,
    val name: String,
    val language: String,
    val stars: Int,
    val description: String,
    val url: String,
    val forked: Boolean
)