package com.andika.octofriends.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {

    private const val BASE_URL = "https://api.github.com/"
    private const val token = "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"

    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request().newBuilder()
                .header("Authorization", "Bearer $token")
                .build()

            return chain.proceed(request)
        }
    }

    fun getApiService(): ApiService {
        val loggingInterceptor= HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)

        val tokenInterceptor = TokenInterceptor()

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}