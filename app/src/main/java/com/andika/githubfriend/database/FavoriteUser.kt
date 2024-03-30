package com.andika.githubfriend.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class FavoriteUser(
    @PrimaryKey
    @ColumnInfo(name = "username")
    var username: String = "no username",

    @ColumnInfo(name = "name")
    var name: String = "no name",

    @ColumnInfo(name = "picture_url")
    var pictureUrl: String? = null,

    @ColumnInfo(name = "type")
    var type: String = "User"
) : Parcelable
