package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, S : UiState, I : UiIntent, E : UiEffect, VM : BaseViewModel<S, I, E>>(
	@LayoutRes layoutId: Int
) : Fragment(layoutId) {

	// Delegate từ thư viện giúp dọn dẹp binding tự động
	protected abstract val binding: VB

	protected abstract val viewModel: VM


	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initViews()
		observeViewModel()
	}

	private fun observeViewModel() {
		viewLifecycleOwner.lifecycleScope.launch {
			viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
				// 1. Thu thập và vẽ UI từ State
				launch {
					viewModel.uiState.collect { state ->
						render(state)
					}
				}

				// 2. Thu thập và xử lý Effect (Sự kiện 1 lần)
				launch {
					viewModel.effect.collect { effect ->
						handleEffect(effect)
					}
				}
			}
		}
	}

	// Gửi Intent sang ViewModel một cách ngắn gọn
	protected fun sendIntent(intent: I) {
		viewModel.sendIntent(intent)
	}

	// Lớp con bắt buộc triển khai để vẽ giao diện
	protected abstract fun render(state: S)

	// Lớp con triển khai để xử lý Toast, Navigate, v.v.
	protected abstract fun handleEffect(effect: E)

	// Lớp con có thể khởi tạo Click Listener, Adapter tại đây
	protected open fun initViews() {}
}