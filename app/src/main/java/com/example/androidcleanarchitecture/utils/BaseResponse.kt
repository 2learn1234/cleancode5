package com.example.androidcleanarchitecture.utils

data class BaseResponse<T>(
    val data: T?,
    val errorMessage: String? = null
)