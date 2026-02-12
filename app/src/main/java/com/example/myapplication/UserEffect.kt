package com.example.myapplication

import com.example.myapplication.model.User

sealed class UserEffect {
    data class ShowToast(val message: String) : UserEffect()
    data class NavigateToDetail(val user: User): UserEffect()

}