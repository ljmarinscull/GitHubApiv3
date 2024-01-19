package com.example.githubapiv3.domain.repository

import com.example.githubapiv3.domain.models.Repository
import com.example.githubapiv3.domain.models.User
import com.example.githubapiv3.domain.models.UserDetails

interface IUserRepository {
    suspend fun getUsers(): Result<List<User>>
    suspend fun getUserDetails(url: String): Result<UserDetails>
    suspend fun getRepositoriesByUser(url: String): Result<List<Repository>>
}