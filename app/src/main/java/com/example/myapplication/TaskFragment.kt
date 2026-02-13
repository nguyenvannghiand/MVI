package com.example.myapplication

import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myapplication.databinding.FragmentTaskBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : BaseFragment<FragmentTaskBinding, TaskState, TaskIntent, TaskEffect, TaskViewModel>(
	R.layout.fragment_task
) {
	override val binding by viewBinding(FragmentTaskBinding::bind)
	override val viewModel: TaskViewModel by viewModels()

	override fun initViews() {
		// Gửi Intent khi bấm nút
		binding.btnRefresh.setOnClickListener {
			sendIntent(TaskIntent.LoadTasks)
		}
	}

	override fun render(state: TaskState) {
		binding.progressBar.isVisible = state.isLoading
		//taskAdapter.submitList(state.tasks)
	}

	override fun handleEffect(effect: TaskEffect) {
		when (effect) {
			is TaskEffect.ShowToast -> Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
		}
	}
}