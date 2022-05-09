package com.arepade.oze.network

import com.arepade.oze.dataModels.GithubResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface Github {

    @GET("search/users")
    suspend fun getUsersAsync(
        @Query("q") location : String = "lagos",
        @Query("page") page : String

    ): GithubResponse


}