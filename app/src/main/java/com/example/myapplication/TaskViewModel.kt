package com.example.myapplication

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor() : BaseViewModel<TaskState, TaskIntent, TaskEffect>() {

	fun addTask(name: String) {
		setState {
			copy(isLoading = false, tasks = tasks.plus(name))
		}
	}

	override fun createInitialState(): TaskState = TaskState()

	override fun handleIntent(intent: TaskIntent) {
		when(intent){
			is TaskIntent.AddTask -> addTask(intent.name)
			TaskIntent.LoadTasks -> {
				//loadTasks()
			}

		}
	}

}