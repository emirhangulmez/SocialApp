package com.emirhan.socialapp.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.use_case.home.CreateStoryUseCase
import com.emirhan.socialapp.domain.use_case.home.GetPostsUseCase
import com.emirhan.socialapp.domain.use_case.home.GetStoriesUseCase
import com.emirhan.socialapp.presentation.home.model.PostState
import com.emirhan.socialapp.presentation.home.model.StoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPostsUseCase: GetPostsUseCase,
    private val getStoriesUseCase: GetStoriesUseCase,
    private val createStoryUseCase: CreateStoryUseCase
) : ViewModel() {

    /*
    *  Text Fields Recompose Strings for Login
    */
    var commentValue = mutableStateOf("")
        private set

    // Post State for check posts last state
    private val _postState = mutableStateOf(PostState())
    val postState: State<PostState> = _postState

    // Stories State for check stories last state
    private val _storiesState = mutableStateOf(StoryState())
    val storiesState: State<StoryState> = _storiesState

    // Story State for check story last state
    private val _storyState = mutableStateOf(StoryState())
    val storyState: State<StoryState> = _storyState

    // Get posts in app starting
    init {
        getPosts()
        getStories()
    }

    // onValueChanged Listeners
    fun commentValue(value: String) {
        commentValue.value = value
    }

    private fun getPosts() =
        getPostsUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _postState.value = PostState(posts = result.data)
                }
                is Resource.Error -> {
                    _postState.value = PostState(error = result.message)
                }
                is Resource.Loading -> {
                    _postState.value = PostState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    private fun getStories() =
        getStoriesUseCase().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _storiesState.value = StoryState(stories = result.data)
                }
                is Resource.Error -> {
                    _storiesState.value = StoryState(error = result.message)
                }
                is Resource.Loading -> {
                    _storiesState.value = StoryState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    fun createStory(story: Story) {
        viewModelScope.launch {
            createStoryUseCase(story).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _storyState.value = StoryState(story = result.data)
                    }
                    is Resource.Error -> {
                        _storyState.value = StoryState(error = result.message)
                    }
                    is Resource.Loading -> {
                        _storyState.value = StoryState(isLoading = true)
                    }
                }
            }
        }
    }
}