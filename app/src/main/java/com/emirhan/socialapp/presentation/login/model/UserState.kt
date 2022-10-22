package com.emirhan.socialapp.presentation.login.model

import com.emirhan.socialapp.domain.model.User

data class UserState (
    val error: String = "",
    val user : User? = null,
    val users : List<User?>? = null,
    val isLoading: Boolean = false
)