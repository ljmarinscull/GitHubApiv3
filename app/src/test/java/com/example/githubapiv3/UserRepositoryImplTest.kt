package com.example.githubapiv3

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import com.example.githubapiv3.domain.repository.UserRepositoryImpl
import com.example.githubapiv3.utils.mappers.RepositoryListMapper
import com.example.githubapiv3.utils.mappers.RepositoryMapper
import com.example.githubapiv3.utils.mappers.UserDetailsMapper
import com.example.githubapiv3.utils.mappers.UserListMapper
import com.example.githubapiv3.utils.mappers.UserMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException

class UserRepositoryImplTest {

    private lateinit var repository: UserRepositoryImpl

    private lateinit var api: ApiServiceFake
    private lateinit var userMapper: UserListMapper
    private lateinit var userDetailsMapper: UserDetailsMapper
    private lateinit var repositoryListMapper: RepositoryListMapper

    @BeforeEach
    fun setUp() {
        api = ApiServiceFake()
        userMapper = UserListMapper(UserMapper)
        userDetailsMapper = UserDetailsMapper
        repositoryListMapper = RepositoryListMapper(RepositoryMapper)

        repository = UserRepositoryImpl(
            api,
            userMapper,
            userDetailsMapper,
            repositoryListMapper
        )
    }

    @Test
    fun `Repository returns a list of users error, return failure`() = runBlocking {
        val usersResult = repository.getUsers()

        assertThat(usersResult.isSuccess).isTrue()
        assertThat(usersResult.getOrThrow().size).isEqualTo(api.users.size)
    }

    @Test
    fun `Get repositories by user is success but the items are filtered`() = runBlocking {
        val usersResult = repository.getRepositoriesByUser("")

        assertThat(usersResult.isSuccess).isTrue()
        assertThat(usersResult.getOrThrow().size).isNotEqualTo(api.repositories.size)

        val expectedRepositories = repositoryListMapper.map(api.repositories).filter { !it.forked }
        assertThat(usersResult.getOrThrow()).isEqualTo(expectedRepositories)
    }

    @Test
    fun `Repository returns a list of users error, return failurem`() = runBlocking {

        val apiFake: ApiServiceFake = mockk()

        val repository = UserRepositoryImpl(
            apiFake,
            UserListMapper(UserMapper),
            UserDetailsMapper,
            RepositoryListMapper(RepositoryMapper)
        )

        coEvery { apiFake.requestRepositoriesByUser(any()) } throws mockk<HttpException> {
            every { code() } returns 404
            every { message() } returns "Test message"
        }

        val result = repository.getRepositoriesByUser("")

        assertThat(result.isFailure).isTrue()
    }

    @Test
    fun `Repository returns a list of users error, return failuremmj`() = runBlocking {
        val url = api.users.first().url
        val apiFake: ApiServiceFake = mockk()

        val repository = UserRepositoryImpl(
            apiFake,
            UserListMapper(UserMapper),
            UserDetailsMapper,
            RepositoryListMapper(RepositoryMapper)
        )
        coEvery {
            apiFake.requestRepositoriesByUser(any())
        } returns api.repositories

        val result = repository.getRepositoriesByUser(url)

        coVerify {
            apiFake.requestRepositoriesByUser(match{ url == it })
        }

        assertThat(result.isSuccess).isTrue()
    }

    @Test
    fun `Repository returns a list of users error, return failuremm`() = runBlocking {
        val url = api.users.first().url
        val apiFake: ApiServiceFake = mockk()

        val repository = UserRepositoryImpl(
            apiFake,
            UserListMapper(UserMapper),
            UserDetailsMapper,
            RepositoryListMapper(RepositoryMapper)
        )
        coEvery {
            apiFake.requestUserDetails(any())
        } returns api.userDetails

        val result = repository.getUserDetails(url)

        coVerify {
            apiFake.requestUserDetails(match{ url == it })
        }

        assertThat(result.isSuccess).isTrue()
    }
}