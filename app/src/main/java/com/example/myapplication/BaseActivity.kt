package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BaseActivity<VB : ViewBinding, S : UiState, I : UiIntent, E : UiEffect, VM : BaseViewModel<S, I, E>>(
	private val bindingFactory: (LayoutInflater) -> VB
) : AppCompatActivity() {

	// 2. Sử dụng 'by lazy': Chỉ khởi tạo khi dùng lần đầu, và KHÔNG bao giờ null
	protected val binding: VB by lazy { bindingFactory(layoutInflater) }

	protected abstract val viewModel: VM

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// binding sẽ được khởi tạo ngay dòng này
		setContentView(binding.root)

		initView()
		observeViewModel()
	}

	abstract fun initView()

	private fun observeViewModel() {
		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.uiState.collect { render(it) }
			}
		}

		lifecycleScope.launch {
			repeatOnLifecycle(Lifecycle.State.STARTED) {
				viewModel.effect.collect { handleEffect(it) }
			}
		}
	}

	abstract fun render(state: S)
	abstract fun handleEffect(effect: E)

	// Lưu ý: Với Activity dùng by lazy, ta KHÔNG CẦN set binding = null trong onDestroy nữa
}