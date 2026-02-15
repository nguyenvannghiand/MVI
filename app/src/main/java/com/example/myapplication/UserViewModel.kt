package com.example.myapplication

import androidx.lifecycle.viewModelScope
import com.example.myapplication.di.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): BaseViewModel<UserState, UserIntent, UserEffect>() {


	override fun createInitialState(): UserState  = UserState()


	override fun handleIntent(intent: UserIntent) {
		when(intent){
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
        viewModelScope.launch {
	        setState { copy(isLoading = true) }
	        try {
		        val users = userRepository.getUsers()
		        setState { copy(isLoading = false, listUsers = users) }
		        setEffect { UserEffect.ShowToast("Success!") }
	        } catch (e: Exception) {
		        setState { copy(isLoading = false, error = e.message) }
	        }
        }
    }
}