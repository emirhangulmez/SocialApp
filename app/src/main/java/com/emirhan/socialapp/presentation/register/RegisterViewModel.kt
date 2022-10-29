package com.emirhan.socialapp.presentation.register

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emirhan.socialapp.core.Constants.Companion.USERNAME_REGEX
import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.domain.use_cases.login.RegisterUseCase
import com.emirhan.socialapp.presentation.register.model.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {
    private companion object {
        // This regex provides correct username from specific rules
        @JvmStatic
        val usernameRegex = USERNAME_REGEX
    }

    // Register State for the only purpose registering
    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState> = _state

    /*
    *  Text Fields Recompose Strings for Registering
    */
    var usernameText by mutableStateOf("")
        private set

    var emailText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    // Placeholder Validation Check
    fun isEmailValid(): Boolean =
        if (emailText.isNotBlank() && passwordText.isNotBlank() && usernameText.isNotBlank()) {
            Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        } else {
            true
        }

    fun isPasswordValid(): Boolean =
        if (emailText.isNotBlank() && passwordText.isNotBlank() && usernameText.isNotBlank()) {
            passwordText.length > 5
        } else {
            true
        }

    fun isUsernameValid(): Boolean =
        if (emailText.isNotBlank() && passwordText.isNotBlank() && usernameText.isNotBlank()) {
            usernameRegex.toRegex().matches(usernameText)
        } else {
            true
        }

    // This function is validation check result
    fun checkValidation(): Boolean =
        usernameRegex.toRegex().matches(usernameText) && Patterns.EMAIL_ADDRESS.matcher(emailText)
            .matches() && passwordText.length > 5

    // Clearing User Filled Form for the security reasons.
    private fun clearActions() {
        RegisterState(user = null)
        // Form Fields
        usernameText = mutableStateOf("").toString()
        emailText = mutableStateOf("").toString()
        passwordText = mutableStateOf("").toString()
    }

    // TextField onValueChanged Listeners
    fun onEmailChanged(value: String) {
        emailText = value
    }

    fun onPasswordChanged(value: String) {
        passwordText = value
    }

    fun onUsernameChanged(value: String) {
        usernameText = value
    }

    // Register use case for the registering and control to the registering state
    fun registerUser() = viewModelScope.launch {
        registerUseCase(usernameText, emailText, passwordText).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = RegisterState(user = result.data).also {
                        clearActions()
                    }
                }
                is Resource.Error -> {
                    _state.value = RegisterState(error = result.message.toString())
                }
                is Resource.Loading -> {
                    _state.value = RegisterState(isLoading = true)
                }
            }
        }
    }
}