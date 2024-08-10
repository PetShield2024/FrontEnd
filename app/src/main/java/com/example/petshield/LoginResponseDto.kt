package com.example.petshield

data class LoginResponseDto(
    val loginSuccess: Boolean,
    val user: User?
)

data class User(
    val id: Long,
    val nickname: String?,
    val email: String?
)
