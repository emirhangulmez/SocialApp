package com.emirhan.socialapp.domain.use_case.login

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserStateUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(): Flow<Resource<FirebaseUser?>> = flow {
        try {
            emit(Resource.Loading())
            when (val user = repository.currentUser()) {
                null -> {
                    emit(Resource.Success(null))
                }
                else -> {
                    emit(Resource.Success(user))
                }
            }
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}