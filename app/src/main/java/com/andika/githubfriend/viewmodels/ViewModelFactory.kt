package com.andika.githubfriend.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.andika.githubfriend.preferences.SettingPreferences

class ViewModelFactory(
    private val pref: SettingPreferences, private val application: Application
) : NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(pref: SettingPreferences, application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(pref, application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref = pref) as T
            }

            modelClass.isAssignableFrom(FavoriteUserViewModel::class.java) -> {
                FavoriteUserViewModel(application = application) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

        }
    }
}