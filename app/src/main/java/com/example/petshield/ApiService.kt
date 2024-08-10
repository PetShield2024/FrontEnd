package com.example.petshield

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("callback")
    fun getKakaoToken(@Query("code") code: String): Call<KakaoTokenDto>

    @POST("callback")
    fun loginWithKakao(@Body token: String?): Call<LoginResponseDto>
}
