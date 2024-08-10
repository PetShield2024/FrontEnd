package com.example.petshield

data class KakaoTokenDto(
    val accessToken: String?,
    val tokenType: String?,
    val expiresIn: Long?,
    val refreshToken: String?,
    val refreshTokenExpiresIn: Long?
)
