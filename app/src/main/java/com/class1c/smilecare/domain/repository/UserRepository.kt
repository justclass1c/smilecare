package com.class1c.smilecare.domain.repository

import com.class1c.smilecare.domain.model.user.User

interface UserRepository {
    suspend fun registerUser(user: User): String // string type to return userID
    suspend fun loginUser(email: String, password: String): User?
    suspend fun listUsers(category: Int): List<User>
    suspend fun updateUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun getUserById(userId: String): User?
}
