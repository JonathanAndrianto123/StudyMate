package com.example.studymate.data.repository

import com.example.studymate.data.local.dao.UserDao
import com.example.studymate.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val dao: UserDao
) {
    suspend fun registerUser(user: UserEntity): Long {
        return dao.insertUser(user)
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        val user = dao.getUserByEmail(email)
        return if (user != null && org.mindrot.jbcrypt.BCrypt.checkpw(password, user.password)) {
            user
        } else {
            null
        }
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return dao.getUserByEmail(email)
    }

    fun getUserById(userId: Int): Flow<UserEntity?> {
        return dao.getUserById(userId)
    }

    suspend fun updateUser(user: UserEntity) {
        dao.updateUser(user)
    }

    suspend fun hasUsers(): Boolean {
        return dao.getUserCount() > 0
    }
}
