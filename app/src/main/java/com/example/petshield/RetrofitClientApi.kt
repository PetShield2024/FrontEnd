package com.example.petshield

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientApi {
    private const val BASE_URL = "http://192.168.42.106:8080/"

    private var retrofit: Retrofit? = null

    val retrofitInterface: RetrofitInterface
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(RetrofitInterface::class.java)
        }
}
