package com.emirhan.socialapp.domain.use_case.login

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.repository.LoginRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(uid: String?): Flow<Resource<User?>> = flow {
        try {
            emit(Resource.Loading())
            emit(
                Resource.Success(
                    repository.getUser(uid)
                )
            )
        } catch (e: FirebaseException) {
            emit(Resource.Error(message = e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}