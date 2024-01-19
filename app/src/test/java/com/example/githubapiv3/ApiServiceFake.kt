package com.example.githubapiv3

import com.example.githubapiv3.data.api.ApiService
import com.example.githubapiv3.data.models.Parent
import com.example.githubapiv3.data.models.RepositoryJO
import com.example.githubapiv3.data.models.Source
import com.example.githubapiv3.data.models.UserDetailsJO
import com.example.githubapiv3.data.models.UserJO

class ApiServiceFake: ApiService {

    val users = listOf(
        UserJO("admin", "", "", "", "", ""),
        UserJO("admin1", "", "", "", "", ""),
        UserJO("admin2", "", "", "", "", ""),
        UserJO("admin3", "", "", "", "", "")
    )

    val userDetails = UserDetailsJO("admin", following = 1, followers = 2)

    val repositories = listOf(
        RepositoryJO(
            id = 1,
            name = "",
            language = "",
            stargazers_count = 1,
            description = "",
            html_url = "",
            parent = Parent(id = 1),
            source = Source(id = 1)
        ),
        RepositoryJO(
            id = 2,
            name = "",
            language = "",
            stargazers_count = 2,
            description = "",
            html_url = "",
            parent = null,
            source = null
        ),
        RepositoryJO(
            id = 3,
            name = "",
            language = "",
            stargazers_count = 3,
            description = "",
            html_url = "",
            parent = null,
            source = null
        ),
        RepositoryJO(
            id = 4,
            name = "",
            language = "",
            stargazers_count = 4,
            description = "",
            html_url = "",
            parent = null,
            source = null
        )
    )

    override suspend fun requestUsers() = users

    override suspend fun requestRepositoriesByUser(url: String) = repositories

    override suspend fun requestUserDetails(url: String) = userDetails
}