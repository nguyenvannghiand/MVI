package com.example.myapplication

sealed class UserEffect {
    data class ShowToast(val message: String) : UserEffect()
    object NavigateToDetail: UserEffect()
}