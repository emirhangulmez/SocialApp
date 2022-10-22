package com.emirhan.socialapp.presentation.register.model

import com.google.firebase.auth.FirebaseUser

data class RegisterState (
    val isLoading: Boolean = false,
    val user: FirebaseUser? = null,
    val error: String = ""
)