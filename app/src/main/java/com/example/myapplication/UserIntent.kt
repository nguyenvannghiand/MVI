package com.example.myapplication

import com.example.myapplication.model.User

sealed class UserIntent: UiIntent {
	object FetchDataUsers: UserIntent()
	data class ClickUser(val user: User): UserIntent()
	object CloseDetail: UserIntent()
}