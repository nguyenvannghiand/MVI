package com.example.myapplication

import com.example.myapplication.model.User

// 1. ViewState: Dai dien cho tat ca nhung gi hien thi tren man hinh
data class UserState(
	val isLoading: Boolean = false,
	val listUsers: List<User> = emptyList(),
	val isDetailVisible: Boolean = false,
	val selectedUser: User? = null,
	val error: String? = null
): UiState