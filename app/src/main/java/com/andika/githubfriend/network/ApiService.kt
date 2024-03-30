package com.andika.githubfriend.network

import com.andika.githubfriend.model.SearchResponse
import com.andika.githubfriend.model.UserDetailResponse
import com.andika.githubfriend.model.UserFollowersResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun search(@Query("q") username: String): Call<SearchResponse>

    @GET("users/{username}")
    fun getUserDetail(@Path("username") username: String): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<ArrayList<UserFollowersResponse>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<UserFollowersResponse>>
}