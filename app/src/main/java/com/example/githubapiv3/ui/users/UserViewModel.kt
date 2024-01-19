package com.example.githubapiv3.ui.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.githubapiv3.domain.models.Repository
import com.example.githubapiv3.domain.models.User
import com.example.githubapiv3.domain.models.UserDetails
import com.example.githubapiv3.domain.repository.IUserRepository
import com.example.githubapiv3.utils.Resource
import kotlinx.coroutines.launch

class UserViewModel(
    private val repo: IUserRepository
) : ViewModel() {

    lateinit var selectedUser: User
    fun setUserSelected(user: User) {
        selectedUser = user
    }

    private val _selectedUserDetails = MutableLiveData<Resource<UserDetails>>()
    val selectedUserDetails: LiveData<Resource<UserDetails>> = _selectedUserDetails

    private val _selectedUserRepositories = MutableLiveData<Resource<List<Repository>>>()
    val selectedUserRepositories: LiveData<Resource<List<Repository>>> = _selectedUserRepositories

    val users = liveData {
        emit(Resource.Loading)
        try {
            emit(Resource.Success(repo.getUsers().getOrThrow()))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    fun getUserDetails() {
        viewModelScope.launch {
            _selectedUserDetails.postValue(Resource.Loading)
            try {
                _selectedUserDetails.postValue(
                    Resource.Success(
                        repo.getUserDetails(selectedUser.details).getOrThrow()
                    )
                )
            } catch (e: Exception) {
                _selectedUserDetails.postValue(Resource.Error(e))
                _selectedUserDetails.postValue(Resource.Success(UserDetails.emptyData()))
            }
        }
    }

    fun getUserRepositories() {
        viewModelScope.launch {
            _selectedUserRepositories.postValue(Resource.Loading)
            try {
                _selectedUserRepositories.postValue(
                    Resource.Success(
                        repo.getRepositoriesByUser(selectedUser.repos).getOrThrow()
                    )
                )
            } catch (e: Exception) {
                _selectedUserRepositories.postValue(Resource.Error(e))
            }
        }
    }

}