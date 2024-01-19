package com.example.githubapiv3.data.api

import com.example.githubapiv3.data.models.RepositoryJO
import com.example.githubapiv3.data.models.UserDetailsJO
import com.example.githubapiv3.data.models.UserJO
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface ApiService {
    @Headers(
        "Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET("/users")
    suspend fun requestUsers(): List<UserJO>

    @Headers(
        "Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET
    suspend fun requestRepositoriesByUser(@Url url: String): List<RepositoryJO>

    @Headers(
        "Accept: application/vnd.github+json",
        "X-GitHub-Api-Version: 2022-11-28"
    )
    @GET
    suspend fun requestUserDetails(@Url url: String): UserDetailsJO
}

class RetrofitClient {

    companion object {
        private var instance : ApiService? = null
        private val token = "Bearer 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"
        private val AUTHORIZATION = "Authorization"

        @Synchronized
        fun getInstance(): ApiService {
            if (instance == null){
                val client = OkHttpClient()
                    .newBuilder()
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor { chain ->
                    val request = chain.request()
                    request.newBuilder().addHeader(
                        AUTHORIZATION, token
                    ).build()
                    chain.proceed(request)
                }.build()

                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.github.com")
                    .client(client)
                    .build()
                    .create(ApiService::class.java)
            }
            return instance as ApiService
        }
    }
}