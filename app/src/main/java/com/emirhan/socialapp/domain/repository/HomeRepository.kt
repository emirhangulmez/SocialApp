
package com.emirhan.socialapp.domain.repository

import com.emirhan.socialapp.domain.model.Comment
import com.emirhan.socialapp.domain.model.Post
import com.emirhan.socialapp.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    // Get All Posts
    suspend fun getPosts(): Flow<MutableList<Post>>
    // Get Post by id
    suspend fun getPost(postId: String): Post?
    // Create Post
    suspend fun createPost(post: Post): Post?

    // Get All Stories
    suspend fun getStories(): Flow<MutableList<Story>>
    // Get Story by id
    suspend fun getStory(storyId: String): Story?
    // Create Story
    suspend fun createStory(story: Story): Story?

    // Get All Comments
    suspend fun getComments(): List<Comment>
    // Create Comment
    suspend fun createComment(comment: Comment): Comment?
}