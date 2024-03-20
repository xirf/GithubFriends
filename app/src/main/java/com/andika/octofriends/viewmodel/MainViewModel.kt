package com.andika.octofriends.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andika.octofriends.model.Items
import com.andika.octofriends.model.SearchResponse
import com.andika.octofriends.model.UserDetailResponse
import com.andika.octofriends.model.UserFollowersResponse
import com.andika.octofriends.network.ApiConfig
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response


class MainViewModel : ViewModel() {
    private val searchList = MutableLiveData<ArrayList<Items>>()
    private val followers = MutableLiveData<ArrayList<UserFollowersResponse>>()
    private val followings = MutableLiveData<ArrayList<UserFollowersResponse>>()
    private val userDetail = MutableLiveData<UserDetailResponse>()
    private val loadingState = MutableLiveData<Boolean>()

    // Getters
    val getSearchResults: LiveData<ArrayList<Items>> = searchList
    val getUserFollowers: LiveData<ArrayList<UserFollowersResponse>> = followers
    val getUserFollowings: LiveData<ArrayList<UserFollowersResponse>> = followings
    val getUserDetail: LiveData<UserDetailResponse> = userDetail
    val getLoadingState: LiveData<Boolean> = loadingState

    fun searchUser(username: String) {
        Log.d("MainViewModel", "Searching for user $username")
        loadingState.value = true
        val client = ApiConfig.getApiService().search(username)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                val responseBody = response.body()
                Log.d("MainViewModel", "onResponse: $responseBody")
                if (response.isSuccessful && responseBody != null) {
                    searchList.value = ArrayList(responseBody.items)
                    loadingState.value = false
                }else{
                    loadingState.value = false
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                loadingState.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserDetail(username: String) {
        loadingState.value = true
        val client = ApiConfig.getApiService().getUserDetail(username)
        client.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    userDetail.value = responseBody!!
                    loadingState.value = false
                }
            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                loadingState.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowers(username: String) {
        loadingState.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<ArrayList<UserFollowersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserFollowersResponse>>,
                response: Response<ArrayList<UserFollowersResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    followers.value = response.body()
                    loadingState.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UserFollowersResponse>>, t: Throwable) {
                loadingState.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun getUserFollowings(username: String) {
        loadingState.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<ArrayList<UserFollowersResponse>> {
            override fun onResponse(
                call: Call<ArrayList<UserFollowersResponse>>,
                response: Response<ArrayList<UserFollowersResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    followings.value = response.body()
                    loadingState.value = false
                }
            }

            override fun onFailure(call: Call<ArrayList<UserFollowersResponse>>, t: Throwable) {
                loadingState.value = false
                Log.e("MainViewModel", "onFailure: ${t.message}")
            }
        })
    }

    fun detailUser(username: String) {

    }
}