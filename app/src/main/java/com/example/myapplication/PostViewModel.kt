package com.example.myapplication

import androidx.lifecycle.ViewModel
import com.example.myapplication.model.PostState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class PostViewModel: ViewModel() {
	private val _state = MutableStateFlow(PostState())
	val state = _state.asStateFlow()

	fun toggleLike() {
		// TODO: Viết hàm setState tại đây
		// Nếu đã like -> chuyen sang trang thai chua Like: isLiked -> = false, likeCount - 1
		// Nếu chưa like -> chuyen sang trang thai Like: isLiked = true, likeCount + 1

		setState {
			if (isLiked) {
				copy(isLiked = false, likeCount = likeCount - 1)
			} else {
				copy(isLiked = true, likeCount = likeCount + 1)
			}
		}
	}

	private fun setState(reduce: PostState.() -> PostState) {
		_state.update { it.reduce() }
	}
}