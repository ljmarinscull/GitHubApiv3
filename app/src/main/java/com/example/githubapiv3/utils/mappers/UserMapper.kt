package com.example.githubapiv3.utils.mappers

import com.example.githubapiv3.data.models.UserJO
import com.example.githubapiv3.domain.models.User

object UserMapper : IMapper<UserJO, User> {
    override fun map(input: UserJO) = User(
        username = input.login,
        details = input.url,
        avatar = input.avatar_url,
        following = input.following_url,
        followers = input.followers_url,
        repos = input.repos_url
    )
}

class UserListMapper (
    private val mapper: IMapper<UserJO, User>
) : IListMapper<UserJO, User> {
    override fun map(input: List<UserJO>): List<User> {
        return input.map { mapper.map(it) }
    }
}