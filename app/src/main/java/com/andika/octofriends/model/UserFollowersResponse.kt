package com.andika.octofriends.model

import com.google.gson.annotations.SerializedName

data class UserFollowersResponse(
    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("node_id")
    val nodeId: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("gravatar_id")
    val gravatarId: String,

    @field:SerializedName("url")
    val url: String,

    @field:SerializedName("html_url")
    val htmlUrl: String,

    @field:SerializedName("followers_url")
    val followersUrl: String,

    @field:SerializedName("following_url")
    val followingUrl: String,

    @field:SerializedName("gists_url")
    val gistsUrl: String,

    @field:SerializedName("starred_url")
    val starredUrl: String,

    @field:SerializedName("subscriptions_url")
    val subscriptionsUrl: String,

    @field:SerializedName("organizations_url")
    val organizationsUrl: String,

    @field:SerializedName("repos_url")
    val reposUrl: String,

    @field:SerializedName("events_url")
    val eventsUrl: String,

    @field:SerializedName("received_events_url")
    val receivedEventsUrl: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("site_admin")
    val siteAdmin: Boolean
)
