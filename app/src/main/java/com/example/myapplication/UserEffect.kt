package com.example.myapplication


sealed class UserEffect: UiEffect {
    data class ShowToast(val message: String) : UserEffect()
    data class TrackEvent(val event: AnalyticsEvent) : UserEffect() // Thêm dòng này
}

sealed class AnalyticsEvent(val name: String, val params: Map<String, Any> = emptyMap()) {
	data class  Download(val itemId: String) : AnalyticsEvent("user_download", mapOf("item_id" to itemId))
	data class Favorite(val itemId: String) : AnalyticsEvent("user_favorite", mapOf("item_id" to itemId))
	data class  SettingOpen(val itemId: String) : AnalyticsEvent("setting_open", mapOf("item_id" to itemId))
	data class Play(val itemId: String) : AnalyticsEvent("play_media", mapOf("item_id" to itemId))
}