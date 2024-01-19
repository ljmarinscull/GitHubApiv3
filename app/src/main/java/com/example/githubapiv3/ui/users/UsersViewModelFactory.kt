package com.example.githubapiv3.ui.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.githubapiv3.domain.repository.IUserRepository

class UsersViewModelFactory(
    private val repo : IUserRepository
): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                return UserViewModel(
                    repo = repo
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }