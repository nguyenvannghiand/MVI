package com.example.myapplication

import androidx.lifecycle.viewModelScope
import coil.util.CoilUtils.result
import com.example.myapplication.di.repo.UserRepository
import com.example.myapplication.usecase.GetUsersUseCase
import com.example.myapplication.usecase.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
	private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel<UserState, UserIntent, UserEffect>() {


	override fun createInitialState(): UserState = UserState()


	override fun handleIntent(intent: UserIntent) {
		when (intent) {
			is UserIntent.FetchDataUsers -> fetchDataUsers()
			is UserIntent.ClickUser -> {
				setState { copy(isDetailVisible = true, selectedUser = intent.user) }
				setEffect { UserEffect.TrackEvent(AnalyticsEvent.Play(itemId = intent.user.id.toString())) }
			}

			is UserIntent.CloseDetail -> {
				setState { copy(isDetailVisible = false, selectedUser = null) }
			}
		}
	}


	private fun fetchDataUsers() {
		getUsersUseCase.invoke().onEach { result ->
			when (result) {
				is Resource.Loading -> setState { copy(isLoading = true) }
				is Resource.Success -> setState { copy(listUsers = result.data, isLoading = false) }
				is Resource.Error -> {
					setState { copy(isLoading = false) }
					setEffect { UserEffect.ShowToast(result.message) }
				}
			}
		}.launchIn(viewModelScope)
	}
}