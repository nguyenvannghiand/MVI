package com.example.myapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.di.repo.UserRepository
import com.example.myapplication.model.UserIntent
import com.example.myapplication.model.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {

    // State: Luu tru trang thai
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Effect: Cac su kien xay ra mot lan
    private val _effect = Channel<UserEffect>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()

    // Intent: Nhan lenh tu view
    val userIntent = Channel<UserIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent(){
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is UserIntent.FetchDataUsers -> fetchDataUsers()
                    is UserIntent.RemoveUser -> {/* Logic xóa */}
                    is UserIntent.ClickUser -> {
                        _state.update { it.copy(isDetailVisible = true, selectedUser = intent.user) }
                        // Vẫn có thể gửi Effect nếu bạn muốn làm thêm việc khác (như Analytics)
                    }

                    is UserIntent.CloseDetail -> {
                        _state.update { it.copy(isDetailVisible = false, selectedUser = null) }
                    }
                }

            }
        }
    }

    private fun fetchDataUsers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val listUsers = userRepository.getUsers()
                _state.update { it.copy(isLoading = false, listUsers = listUsers) }
                // Gui 1 effect thong bao thanh cong
                _effect.send(UserEffect.ShowToast("Fetch data success"))
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(UserEffect.ShowToast("Fetch data failed"))
            }
        }
    }
}