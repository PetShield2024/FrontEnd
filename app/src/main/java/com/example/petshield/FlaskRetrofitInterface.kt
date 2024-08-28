package com.example.petshield

import retrofit2.Call
import retrofit2.http.GET

interface FlaskRetrofitInterface {
    @GET("/graph")
    fun getGraphImages(): Call<ApiResponse<List<String>>>

    // Define other Flask endpoints here
}
