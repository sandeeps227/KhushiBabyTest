package com.example.khushibabytest.ui.event

sealed class UIResult<out T> {
        data class Success<out T>(val data: T) : UIResult<T>()
        data class Failure(val exception: Exception) : UIResult<Nothing>()
    }