package com.emirhan.socialapp.domain.use_case.login

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(
        username: String,
        email: String,
        password: String
    ): Flow<Resource<FirebaseUser?>> = flow {
        try {
            emit(Resource.Loading())
            val user = repository.register(username, email, password)
            // Set Display Name
            val profileChangeRequest = UserProfileChangeRequest.Builder().let {
                it.displayName = username
                it.build()
            }
            user?.updateProfile(profileChangeRequest)
            emit(Resource.Success(user))
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}
