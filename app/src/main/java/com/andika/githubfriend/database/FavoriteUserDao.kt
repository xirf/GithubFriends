package com.andika.githubfriend.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favoriteUser: FavoriteUser)

    @Update
    fun update(favoriteUser: FavoriteUser)

    @Delete
    fun delete(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favoriteuser")
    fun getAll(): LiveData<List<FavoriteUser>>

    @Query("SELECT * FROM favoriteuser WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>

    @Query("SELECT EXISTS (SELECT 1 FROM favoriteuser WHERE username = :username)")
    fun checkFavoriteUser(username: String): LiveData<Boolean>

    @Query("SELECT EXISTS (SELECT 1 FROM  favoriteuser WHERE username = :username)")
    fun isInFavorite(username: String): Boolean
}