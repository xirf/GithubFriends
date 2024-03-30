package com.andika.githubfriend

import com.andika.githubfriend.model.SearchResponse
import com.andika.githubfriend.model.UserDetailResponse
import com.andika.githubfriend.model.UserFollowersResponse
import com.andika.githubfriend.network.ApiService
import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mockito.*
import retrofit2.Call
import retrofit2.Response

class ApiServiceTest {

    private val apiService = mock(ApiService::class.java)

    @Test
    fun `search returns expected result`() {
        val expectedResponse = mock(SearchResponse::class.java)
        val call = mock(Call::class.java) as Call<SearchResponse>
        `when`(call.execute()).thenReturn(Response.success(expectedResponse))
        `when`(apiService.search("test")).thenReturn(call)

        val actualResponse = apiService.search("test").execute().body()

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `getUserDetail returns expected result`() {
        val expectedResponse = mock(UserDetailResponse::class.java)
        val call = mock(Call::class.java) as Call<UserDetailResponse>
        `when`(call.execute()).thenReturn(Response.success(expectedResponse))
        `when`(apiService.getUserDetail("test")).thenReturn(call)

        val actualResponse = apiService.getUserDetail("test").execute().body()

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `getFollowers returns expected result`() {
        val expectedResponse = arrayListOf(mock(UserFollowersResponse::class.java))
        val call = mock(Call::class.java) as Call<ArrayList<UserFollowersResponse>>
        `when`(call.execute()).thenReturn(Response.success(expectedResponse))
        `when`(apiService.getFollowers("test")).thenReturn(call)

        val actualResponse = apiService.getFollowers("test").execute().body()

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `getFollowing returns expected result`() {
        val expectedResponse = arrayListOf(mock(UserFollowersResponse::class.java))
        val call = mock(Call::class.java) as Call<ArrayList<UserFollowersResponse>>
        `when`(call.execute()).thenReturn(Response.success(expectedResponse))
        `when`(apiService.getFollowing("test")).thenReturn(call)

        val actualResponse = apiService.getFollowing("test").execute().body()

        assertEquals(expectedResponse, actualResponse)
    }
}