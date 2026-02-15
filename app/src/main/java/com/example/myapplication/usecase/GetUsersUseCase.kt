package com.example.myapplication.usecase

import com.example.myapplication.di.repo.UserRepository
import com.example.myapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

// Một Wrapper đơn giản để đóng gói dữ liệu
sealed class Resource<out T> {
	object Loading : Resource<Nothing>()
	data class Success<out T>(val data: T) : Resource<T>()
	data class Error(val message: String) : Resource<Nothing>()
}

class GetUsersUseCase @Inject constructor(
	private val userRepository: UserRepository

) {
		// Trả về một Flow để ViewModel chỉ việc collect
		operator fun invoke(): Flow<Resource<List<User>>> = flow {
			emit(Resource.Loading)
			try {
				val data = userRepository.getUsers()
				// Tại đây bạn có thể xử lý nghiệp vụ (Business Logic)
				// Ví dụ: Chỉ lấy các user có tên không trống, hoặc sắp xếp theo Alphabet
				val filteredUsers = data.filter { it.name.isNotBlank() }.sortedBy { it.name }
				emit(Resource.Success(filteredUsers))
			} catch (e: Exception) {
				emit(Resource.Error(e.message ?: "Unknown Error"))
			}
		}.flowOn(Dispatchers.IO) // Luôn chạy trên IO thread cho an toàn
}