package com.example.githubapiv3.utils.mappers

import com.example.githubapiv3.data.models.RepositoryJO
import com.example.githubapiv3.domain.models.Repository

object RepositoryMapper : IMapper<RepositoryJO, Repository> {
    override fun map(input: RepositoryJO): Repository {
        return Repository(
            id = input.id,
            name = input.name,
            language = input.language ?: "Unknown",
            stars = input.stargazers_count,
            description = input.description ?: "Unknown",
            forked = input.isForked,
            url = input.html_url
        )
    }
}

class RepositoryListMapper(
    private val mapper: IMapper<RepositoryJO, Repository>
) : IListMapper<RepositoryJO, Repository> {
    override fun map(input: List<RepositoryJO>): List<Repository> {
        return input.map { mapper.map(it) }
    }
}