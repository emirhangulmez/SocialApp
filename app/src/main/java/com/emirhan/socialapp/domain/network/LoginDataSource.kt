
package com.emirhan.socialapp.domain.network

import com.emirhan.socialapp.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginDataSource {

    suspend fun authentication(email: String, password: String): FirebaseUser?

    suspend fun createUser(username: String, email: String, password: String): FirebaseUser?

    suspend fun getUsers(uid: String?): Flow<MutableList<User>>

    suspend fun getUser(uid: String?): User?

    fun currentUser(): FirebaseUser?

    fun signOut()
}