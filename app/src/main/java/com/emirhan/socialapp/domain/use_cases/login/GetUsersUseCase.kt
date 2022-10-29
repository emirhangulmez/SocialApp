package com.emirhan.socialapp.domain.use_cases.login

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(uid: String?): Flow<Resource<MutableList<User>>> = flow {
        try {
            emit(Resource.Loading())
            repository.getUsers(uid).collect {
                emit(Resource.Success(it))
            }
        } catch (e: FirebaseAuthException) {
            emit(Resource.Error(message = e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}