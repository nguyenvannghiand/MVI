package com.example.myapplication

import javax.inject.Inject
import javax.inject.Singleton

interface AnalyticsManager {
	fun track(event: AnalyticsEvent)
}

@Singleton
class AnalyticsManagerImpl @Inject constructor(): AnalyticsManager {
	override fun track(event: AnalyticsEvent) {
		// Thực hiện logic tracking
		println("Log: ${event.name} with params: ${event.params}")
		when(event){
			is AnalyticsEvent.Download -> {
				println("Log: ${event.name} with params: ${event.params}")
			}
			is AnalyticsEvent.Favorite -> {
				println("Log: ${event.name} with params: ${event.params}")
			}
			is AnalyticsEvent.Play -> {
				println("Log: ${event.name} with params: ${event.params}")
			}
			is AnalyticsEvent.SettingOpen -> {
				println("Log: ${event.name} with params: ${event.params}")
			}
		}
	}
}