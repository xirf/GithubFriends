package com.andika.octofriends.model

import com.google.gson.annotations.SerializedName

data class UserFollowersResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,


    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean
)
