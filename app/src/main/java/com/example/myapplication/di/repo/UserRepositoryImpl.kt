package com.example.myapplication.di.repo

import com.example.myapplication.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject

interface  UserRepository {
    suspend fun getUsers(): List<User>
}

class UserRepositoryImpl @Inject constructor(): UserRepository{
    override suspend fun getUsers(): List<User> = withContext(Dispatchers.IO){
        delay(1000)

        (1..9).map { index ->
            User(index, "User $index")
        }
    }
}