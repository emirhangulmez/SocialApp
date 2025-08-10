package com.emirhan.socialapp.domain.repository

import androidx.credentials.Credential
import com.emirhan.socialapp.domain.model.User
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

    suspend fun login(email: String, password: String): FirebaseUser?

    suspend fun register(username: String, email: String, password: String): FirebaseUser?

    suspend fun getUsers(uid: String?): Flow<MutableList<User>>

    suspend fun getUser(uid: String?): User?

    fun currentUser(): FirebaseUser?

    fun signOut()

    suspend fun authenticateWithPasskey(): Credential?
}