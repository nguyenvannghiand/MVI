package com.example.myapplication

import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, UserState, UserIntent, UserEffect, UserViewModel>(
	{ layoutInflater -> ActivityMainBinding.inflate(layoutInflater) }
) {

	@Inject
	lateinit var analyticsManager: AnalyticsManager

	override val viewModel: UserViewModel by viewModels()

	override fun initView() {
		// Gọi binding bình thường, an toàn tuyệt đối
		viewModel.sendIntent(UserIntent.FetchDataUsers)

		setupOnClickListeners()
	}

	override fun render(state: UserState) {
		binding.fragmentContainer.isVisible = state.isDetailVisible
		binding.infoUser.isVisible = !state.isDetailVisible

		if (state.isDetailVisible && state.selectedUser != null) {
			showFragmentDetail(state.selectedUser)
		}
	}

	override fun handleEffect(effect: UserEffect) {
		when (effect) {
			is UserEffect.ShowToast -> Toast.makeText(this, effect.message, Toast.LENGTH_SHORT).show()
			is UserEffect.TrackEvent -> {
				analyticsManager.track(event = effect.event)
			}
		}
	}


	private fun setupOnClickListeners() {
		binding.infoUser.setOnClickListener {
			val user = viewModel.uiState.value.listUsers.firstOrNull()
			user?.let { viewModel.sendIntent(UserIntent.ClickUser(it)) }
		}

		onBackPressedDispatcher.addCallback(this){
			if (supportFragmentManager.backStackEntryCount > 0) {
				supportFragmentManager.popBackStack()
				lifecycleScope.launch {
					viewModel.sendIntent(UserIntent.CloseDetail)
				}
			} else {
				finish()
			}
		}
	}

	// ... các hàm phụ trợ khác (showFragmentDetail, onBackPressed...)
	private fun showFragmentDetail(user: User) {
		// Kiểm tra để tránh add đè nhiều Fragment giống nhau khi render chạy lại
		val currentFrag = supportFragmentManager.findFragmentByTag("UserDetail")
		if (currentFrag == null) {
			val fragment = UserDetailFragment.newInstance(user)
			supportFragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, fragment, "UserDetail")
				.addToBackStack(null)
				.commit()
		}
	}
}