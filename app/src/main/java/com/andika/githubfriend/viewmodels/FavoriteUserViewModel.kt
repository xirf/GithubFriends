package com.andika.githubfriend.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.andika.githubfriend.database.FavoriteUser
import com.andika.githubfriend.database.FavoriteUserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val repository = FavoriteUserRepository(application)

    fun insert(favoriteUser: FavoriteUser) {
        repository.insert(favoriteUser)
    }

    fun delete(favoriteUser: FavoriteUser) {
        repository.delete(favoriteUser)
    }

    fun getAll(): LiveData<List<FavoriteUser>> = repository.getAll()

    fun checkFavoriteUser(username: String): LiveData<Boolean> = repository.checkFavoriteUser(username)

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> =
        repository.getFavoriteUserByUsername(username)
}