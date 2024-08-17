package com.example.petshield

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    // 비만도 사진 등록 API
    @Multipart
    @POST("obesitys/{dogId}/image")
    fun uploadObesityImage(
        @Path("dogId") dogId: Long,
        @Part image: MultipartBody.Part
    ): Call<ApiResponse<ObesityImageResponse>>


    // 비만도 사진 불러오기 API
    @GET("obesitys/{dogId}/get")
    fun getObesityImage(
        @Path("dogId") dogId: Long
    ): Call<ApiResponse<ObesityGetImageResponse>>

    // 강아지 정보 등록 API
    @POST("dogs/{userId}/profile")
    fun registerDogProfile(
        @Path("userId") userId: Long,
        @Body dogProfile: RequestBody
    ): Call<ApiResponse<DogProfileResponse>>

    // 강아지 정보 수정 API
    @PATCH("dogs/{dogId}/modify")
    fun modifyDogProfile(
        @Path("dogId") dogId: Long,
        @Body dogProfile: RequestBody
    ): Call<ApiResponse<DogModifyResponse>>


    // 강아지 사진 등록 API
    @Multipart
    @POST("dogs/{dogId}/image")
    fun registerDogImage(
        @Path("dogId") dogId: Long,
        @Part image: MultipartBody.Part?
    ): Call<ApiResponse<DogImageResponse>>

    // 강아지 사진 수정 API
    @Multipart
    @PATCH("dogs/{dogId}/image/modify")
    fun modifyDogImage(
        @Path("dogId") dogId: Long,
        @Part image: MultipartBody.Part?
    ): Call<ApiResponse<DogImageModifyResponse>>

    // 강아지 사진 조회 API
    @GET("dogs/{dogId}/image/get")
    fun getDogImage(
        @Path("dogId") dogId: Long,
    ): Call<ApiResponse<DogImageGetResponse>>

    // 강아지 프로필 조회 API
    @GET("dogs/{dogId}/home")
    fun getDogHome(
        @Path("dogId") dogId: Long
    ): Call<ApiResponse<DogHomeResponse>>

    // 강아지 정보 조회 API
    @GET("dogs/{dogId}/info")
    fun getDogInfo(
        @Path("dogId") dogId: Long
    ): Call<ApiResponse<DogInfoResponse>>

    // 사료 목록 조회 API
    @GET("foods")
    fun getFoodList(
        @Query("page") page: Int
    ): Call<ApiResponse<FoodListResponse>>

    @POST("myfoods/{dogId}/recommend-food")
    fun getFoodRecommendations(
        @Path("dogId") dogId: Long
    ): Call<FoodRecommendationResponse>
}
