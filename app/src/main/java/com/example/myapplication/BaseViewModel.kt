package com.example.myapplication

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

interface UiState
interface UiIntent
interface UiEffect

abstract class BaseViewModel<S: UiState, I: UiIntent, E: UiEffect>: ViewModel() {
    // trang thai khoi tao
    abstract fun createInitialState(): S

    // State: Luon giu gia tri hien tai cua UI
    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    // Intent: Nhan lenh tu View
    private val _intent = Channel<I>(Channel.UNLIMITED)
    val intent = _intent.receiveAsFlow()

    // Effect: Su kien xay ra 1 lan
    private val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    init {
        subscribeIntents()
    }

    // Lang nghe cac intent duoc gui tu View
    private fun subscribeIntents() {
        viewModelScope.launch {
            intent.collect {
                handleIntent(it)
            }
        }
    }

    // Lớp con sẽ triển khai logic xử lý cụ thể cho từng Intent
    abstract fun handleIntent(intent: I)

    // Hàm tiện ích để gửi Intent từ View
    fun sendIntent(intent: I) {
        viewModelScope.launch {
            _intent.send(intent)
        }
    }

    // Hàm tiện ích để cập nhật State
    protected fun setState(reduce: S.() -> S) {
        _uiState.update { it.reduce() }
    }

    // Hàm tiện ích để gửi Effect
    protected fun setEffect(builder: () -> E) {
        val effectValue = builder()
        viewModelScope.launch {
            _effect.send(effectValue)
        }
    }

}