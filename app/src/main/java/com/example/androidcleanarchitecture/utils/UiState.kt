package com.example.androidcleanarchitecture.utils

sealed class UiState<out T> {
    object Empty : UiState<Nothing>()

    object Loading : UiState<Nothing>()

    data class Success<out T>(val data: T?) : UiState<T>()

    data class Error(val data: String?) : UiState<Nothing>()
}
