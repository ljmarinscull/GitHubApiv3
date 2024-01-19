package com.example.githubapiv3.data.models

data class RepositoryJO(
    val id: Int,
    val name: String,
    val language: String?,
    val stargazers_count: Int,
    val description: String?,
    val html_url: String,
    val parent: Parent?,
    val source: Source?,
){
    val isForked = parent != null && source != null
}

data class Parent(val id: Int)
data class Source(val id: Int)
