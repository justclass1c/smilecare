package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.user.User
import com.class1c.smilecare.domain.model.user.UserRole

interface UserRepository {
    suspend fun registerUser(user: User): String // string type to return userID
    suspend fun loginUser(email: String, password: String): User?
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
}