package com.example.myapplication


sealed class UserEffect: UiEffect {
    data class ShowToast(val message: String) : UserEffect()
    data class TrackEvent(val eventName: String, val params: Map<String, String>) : UserEffect() // Thêm dòng này
}