package com.example.myapplication.model

// 1. ViewState: Dai dien cho tat ca nhung gi hien thi tren man hinh
data class UserState(
    val isLoading: Boolean = false,
    val listUsers: List<User> = emptyList(),
    val error: String? = null
)

// 2. ViewIntent: Cac hanh dong ma nguoi dung co the thuc hien
sealed class UserIntent{
    object FetchDataUsers: UserIntent()
    data class ClickUser(val user: User): UserIntent()
    data class RemoveUser(val name: String) : UserIntent()
}
