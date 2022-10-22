
package com.emirhan.socialapp.data.repository

import com.emirhan.socialapp.data.network.HomeDataSourceImpl
import com.emirhan.socialapp.domain.repository.HomeRepository
import com.emirhan.socialapp.domain.model.Comment
import com.emirhan.socialapp.domain.model.Post
import com.emirhan.socialapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

class HomeRepositoryImpl(
    private val dataSource: HomeDataSourceImpl
) : HomeRepository {
    override suspend fun getPosts(): Flow<MutableList<Post>> =
        dataSource.getPosts()

    override suspend fun getPost(postId: String): Post? =
        dataSource.getPost(postId)

    override suspend fun createPost(post: Post): Post? =
        dataSource.createPost(post)

    override suspend fun getStories(): Flow<MutableList<Story>> =
        dataSource.getStories()

    override suspend fun getStory(storyId: String): Story? =
        dataSource.getStory(storyId)

    override suspend fun createStory(story: Story): Story? =
        dataSource.createStory(story)

    override suspend fun getComments(): MutableList<Comment> =
        dataSource.getComments()

    override suspend fun createComment(comment: Comment): Comment? =
        dataSource.createComment(comment)
}