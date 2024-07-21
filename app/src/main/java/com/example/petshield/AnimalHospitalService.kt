package com.example.petshield

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AnimalHospitalService {
    @GET("Animalhosptl")
    fun getAnimalHospitals(
        @Query("KEY") apiKey: String,
        @Query("pIndex") pageIndex: Int,
        @Query("pSize") pageSize: Int,
        @Query("SIGUN_NM") sigunCode: String
    ): Call<AnimalHospitalResponse>
}

