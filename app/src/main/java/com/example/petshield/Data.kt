package com.example.petshield

import java.time.LocalDate
import java.time.LocalDateTime


// 비만도 이미지 응답
data class ObesityImageResponse(
    val obesityId: Long,
    val createdAt: String,
)

// 비만도 이미지 조회 응답
data class ObesityGetImageResponse(
    val obesityId: Long,
    val obesityImage: String,
)

// 강아지 프로필 요청
data class DogProfileRequest(
    val dogName: String,
    val gender: String,
    val birth: String,
    val breed: String,
    val weight: Float,
    val size: String,
    val extra: String
)

// 강아지 등록 응답
data class DogProfileResponse(
    val dogId: Long,
    val createdAt: String,
)

// 강아지 정보 수정 응답
data class DogModifyResponse(
    val dogId: Long,
    val updatedAt: String
)

// 강아지 사진 등록 응답
data class DogImageResponse(
    val dogImageId: Long,
    val createdAt: String,
)

// 강아지 사진 수정 응답
data class DogImageModifyResponse(
    val dogImageId: Long,
    val updatedAt: String?
)


// 강아지 사진 조희 응답
data class DogImageGetResponse(
    val dogImageId: Long,
    val imageUrl: String?
)

// 강아지 프로필 응답
data class DogHomeResponse(
    val dogName: String,
    val birth : String,
    val breed: String,
    val weight : Float,
)

// 강아지 정보 조회 응답
data class DogInfoResponse(
    val gender: String,
    val dogName: String,
    val birth : String,
    val breed: String,
    val weight : Float,
    val size: String,
    val extra: String
)

// 사료 목록 응답
data class FoodListResponse(
    val foodList: List<FoodItem>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Long,
    val isFirst: Boolean,
    val isLast: Boolean
)

// 사료 항목
data class FoodItem(
    val foodName: String,
    val brand: String,
    val price: Int,
    val image: String,
    val obesity: String,
    val size: String,
    val age: String,
    val origin: String,
    val site: String,
    val extra: String,
    val createdAt: String
)

// API 응답 공통 구조
// 클라이언트의 ApiResponse 정의
data class ApiResponse<T>(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: T?
)

