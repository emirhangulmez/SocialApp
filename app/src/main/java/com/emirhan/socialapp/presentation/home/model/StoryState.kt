
package com.emirhan.socialapp.presentation.home.model

import com.emirhan.socialapp.domain.model.Story

data class StoryState(
    val isLoading: Boolean = false,
    val story: Story? = null,
    val stories: List<Story>? = null,
    val error: String? = ""
)