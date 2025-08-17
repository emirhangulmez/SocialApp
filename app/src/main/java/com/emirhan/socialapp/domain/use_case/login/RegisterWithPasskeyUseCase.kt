package com.emirhan.socialapp.domain.use_case.login

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterWithPasskeyUseCase @Inject constructor(
    private val repository: LoginRepository
) {

    operator fun invoke(
        username: String
    ): Flow<Resource<FirebaseUser?>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.registerWithPasskey(username)))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }
}