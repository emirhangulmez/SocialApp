package com.emirhan.socialapp.domain.use_case.login

import androidx.credentials.Credential
import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithPasskeyUseCase @Inject constructor(
    private val repository: LoginRepository
) {

    operator fun invoke(): Flow<Resource<Credential?>> = flow {
        try {
            emit(Resource.Loading())
            emit(Resource.Success(repository.authenticateWithPasskey()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message.orEmpty()))
        }
    }

}