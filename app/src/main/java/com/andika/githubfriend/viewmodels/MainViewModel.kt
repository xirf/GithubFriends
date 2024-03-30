package com.andika.githubfriend.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andika.githubfriend.database.FavoriteUser
import com.andika.githubfriend.database.FavoriteUserRepository
import com.andika.githubfriend.model.Items
import com.andika.githubfriend.model.SearchResponse
import com.andika.githubfriend.model.UserDetailResponse
import com.andika.githubfriend.model.UserFollowersResponse
import com.andika.githubfriend.network.ApiConfig
import com.andika.githubfriend.preferences.SettingPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _searchList = MutableLiveData<ArrayList<Items>?>(null)
    val searchList: LiveData<ArrayList<Items>?> = _searchList

    private val _userDetail = MutableLiveData<UserDetailResponse?>(null)
    val userDetail: LiveData<UserDetailResponse?> = _userDetail

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userFollowers = MutableLiveData<ArrayList<UserFollowersResponse>?>(null)
    val userFollowers: LiveData<ArrayList<UserFollowersResponse>?> = _userFollowers

    private val _userFollowing = MutableLiveData<ArrayList<UserFollowersResponse>?>(null)
    val userFollowing: LiveData<ArrayList<UserFollowersResponse>?> = _userFollowing

    fun searchUser(username: String) {
        Log.d("MainViewModel", "Searching for user $username")
        _isLoading.value = true
        val client = ApiConfig.getApiService().search(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val responseBody = response.body()
                Log.d("MainViewModel", "onResponse: $responseBody")
                if (response.isSuccessful && responseBody != null) {
                    _searchList.value = ArrayList(responseBody.items)
                    _isLoading.value = false
                } else {
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _userDetail.value = responseBody!!
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<UserFollowersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserFollowersResponse>>,
                response: Response<ArrayList<UserFollowersResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userFollowers.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UserFollowersResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowings(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<UserFollowersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserFollowersResponse>>,
                response: Response<ArrayList<UserFollowersResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _userFollowing.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UserFollowersResponse>>, t: Throwable) {
                _isLoading.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch { pref.saveThemeSetting(isDarkModeActive) }
    }
}