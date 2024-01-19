package com.example.githubapiv3.utils.mappers

import com.example.githubapiv3.data.models.UserDetailsJO
import com.example.githubapiv3.domain.models.UserDetails
import com.example.githubapiv3.utils.mappers.IMapper

object UserDetailsMapper : IMapper<UserDetailsJO, UserDetails> {
    override fun map(input: UserDetailsJO) = UserDetails(
        name = input.name,
        following = input.following.toString(),
        followers = input.followers.toString(),
    )
}
