package com.andika.githubfriend.model

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("company")
    val company: String?,

    @field:SerializedName("location")
    val location: String?,

    @field:SerializedName("bio")
    val bio: String?,

    @field:SerializedName("public_repos")
    val publicRepos: String,

    @field:SerializedName("followers")
    val followers: String,

    @field:SerializedName("following")
    val following: String,
)
