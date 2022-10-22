
package com.emirhan.socialapp.presentation.home.model

import com.emirhan.socialapp.domain.model.Post

data class PostState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val posts: List<Post>? = null,
    val error: String? = ""
)