package com.example.githubapiv3.domain.repository

import com.example.githubapiv3.data.api.ApiService
import com.example.githubapiv3.data.models.RepositoryJO
import com.example.githubapiv3.data.models.UserDetailsJO
import com.example.githubapiv3.data.models.UserJO
import com.example.githubapiv3.domain.models.Repository
import com.example.githubapiv3.domain.models.User
import com.example.githubapiv3.domain.models.UserDetails
import com.example.githubapiv3.utils.mappers.IListMapper
import com.example.githubapiv3.utils.mappers.IMapper
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

class UserRepositoryImpl (
    private val api: ApiService,
    private val userMapperList: IListMapper<UserJO, User>,
    private val userDetailsMapper: IMapper<UserDetailsJO, UserDetails>,
    private val repositoryMapperList: IListMapper<RepositoryJO, Repository>,
) : IUserRepository {

    private val UNKNOWN_ERROR = "Unknown error"

    override suspend fun getUsers(): Result<List<User>> {
        return coroutineScope {
            val usersResult = async {
                try {
                    Result.success(api.requestUsers())
                } catch (e: retrofit2.HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

            val fetchedUsers = usersResult.await().getOrNull()
            if (fetchedUsers != null) {
                Result.success(
                    userMapperList.map(fetchedUsers)
                )
            } else {
                Result.failure(
                    usersResult.await().exceptionOrNull() ?: Exception(UNKNOWN_ERROR)
                )
            }
        }
    }

    override suspend fun getUserDetails(url: String): Result<UserDetails> {
        return coroutineScope {

            val userDetailsResult = async {
                try {
                    Result.success(api.requestUserDetails(url))
                } catch (e: retrofit2.HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

            val fetchedUserDetails = userDetailsResult.await().getOrNull()
            if (fetchedUserDetails != null) {
                Result.success(
                    userDetailsMapper.map(fetchedUserDetails)
                )
            } else {
                Result.failure(
                    userDetailsResult.await().exceptionOrNull() ?: Exception(UNKNOWN_ERROR)
                )
            }
        }
    }

    override suspend fun getRepositoriesByUser(url: String): Result<List<Repository>> {
        return coroutineScope {

            val userRepositoriesResult = async {
                try {
                    Result.success(api.requestRepositoriesByUser(url))
                } catch (e: retrofit2.HttpException) {
                    Result.failure(e)
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }

            val fetchedUserRepositories = userRepositoriesResult.await().getOrNull()
            if (fetchedUserRepositories != null) {
                Result.success(
                    repositoryMapperList.map(fetchedUserRepositories).filter { !it.forked }
                )
            } else {
                Result.failure(
                    userRepositoriesResult.await().exceptionOrNull() ?: Exception(UNKNOWN_ERROR)
                )
            }
        }
    }
}
