package com.emirhan.socialapp.domain.use_cases.home

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.repository.HomeRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetStoriesUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(): Flow<Resource<List<Story>>> = flow {
        try {
            emit(Resource.Loading())
            repository.getStories().collect {
                emit(Resource.Success(it))
            }
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}