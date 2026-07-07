package com.lifeosai.app.data.repository

import com.lifeosai.app.data.dao.UserDao
import com.lifeosai.app.data.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) {
    fun getUser(): Flow<UserEntity?> {
        return userDao.getUser()
    }

    suspend fun saveUser(user: UserEntity) {
        userDao.insertUser(user)
    }

    suspend fun clearUser() {
        userDao.clearUser()
    }
}
