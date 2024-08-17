package com.example.petshield

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClientApi {
    private const val BASE_URL = "http://192.168.42.106:8080/"

    private var retrofit: Retrofit? = null

    private fun getClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)  // Connection timeout
            .readTimeout(90, TimeUnit.SECONDS)     // Read timeout
            .writeTimeout(90, TimeUnit.SECONDS)    // Write timeout
            .build()
    }

    val retrofitInterface: RetrofitInterface
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getClient())  // Attach the OkHttpClient with timeouts
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(RetrofitInterface::class.java)
        }
}
