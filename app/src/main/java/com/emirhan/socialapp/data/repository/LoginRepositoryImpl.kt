
package com.emirhan.socialapp.data.repository

import com.emirhan.socialapp.data.network.LoginDataSourceImpl
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

class LoginRepositoryImpl(
    private val dataSource: LoginDataSourceImpl
) : LoginRepository {

    override suspend fun login(
        email: String,
        password: String)
    : FirebaseUser? = dataSource.authentication(email, password)

    override suspend fun register(
        username: String,
        email: String,
        password: String
    ): FirebaseUser? = dataSource.createUser(username, email, password)

    override suspend fun getUsers(
        uid: String?
    ): Flow<MutableList<User>> = dataSource.getUsers(uid)

    override suspend fun getUser(
        uid: String?
    ): User? = dataSource.getUser(uid)

    override fun currentUser(): FirebaseUser? = dataSource.currentUser()

    override fun signOut() = dataSource.signOut()
}