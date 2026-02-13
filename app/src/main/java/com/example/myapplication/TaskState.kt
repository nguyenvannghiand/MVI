package com.example.myapplication

data class TaskState(val tasks: List<String> = emptyList(), val isLoading: Boolean = false) : UiState
sealed class TaskIntent : UiIntent {
	object LoadTasks : TaskIntent()
	data class AddTask(val name: String) : TaskIntent()
}
sealed class TaskEffect : UiEffect {
	data class ShowToast(val message: String) : TaskEffect()
}
