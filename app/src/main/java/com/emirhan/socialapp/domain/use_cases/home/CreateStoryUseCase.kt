package com.emirhan.socialapp.domain.use_cases.home

import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.repository.HomeRepository
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateStoryUseCase @Inject constructor(
    private val repository: HomeRepository
) {
    operator fun invoke(story: Story): Flow<Resource<Story?>> = flow {
        try {
            emit(Resource.Loading())
            repository.createStory(story).apply {
                emit(Resource.Success(this))
            }
        } catch (e: FirebaseFirestoreException) {
            emit(Resource.Error(message = e.message.toString()))
        } catch (e: FirebaseNetworkException) {
            emit(Resource.Error(message = e.message.toString()))
        }
    }
}