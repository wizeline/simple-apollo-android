package com.wizeline.simpleapollosample.entities.responses

sealed class ApiResponse<out T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error(val error: Throwable) : ApiResponse<Nothing>()
}
