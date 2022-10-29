package com.emirhan.socialapp.presentation.login


import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.emirhan.socialapp.core.Resource
import com.emirhan.socialapp.presentation.login.model.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.emirhan.socialapp.domain.use_cases.login.*
import com.emirhan.socialapp.presentation.login.model.UserState


import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    // Check Internet Connectivity
    private val connectivityObserver: ConnectivityObserver,
    // Log-In, Sign-Out and Login State
    private val loginUseCase: LoginUseCase,
    private val loginStateUseCase: UserStateUseCase,
    private val signOutUseCase: SignOutUseCase,
    // Get info on user, users
    private val getUsersUseCase: GetUsersUseCase,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {

    /*
    *  Text Fields Recompose Strings for Login
    */
    var emailText by mutableStateOf("")
        private set

    var passwordText by mutableStateOf("")
        private set

    // Login State for check user last state
    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    // User State for get last user data
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    // Get User Detail Data with Mutable State List
    private var _usersState = mutableStateListOf<User?>()
    val usersState: SnapshotStateList<User?> = _usersState

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _connectivityState = MutableStateFlow(ConnectivityObserver.Status.Unavailable)
    val connectivityState = _connectivityState.asStateFlow()


    // Placeholder Validation Check
    fun isEmailValid(): Boolean {
        return if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
            Patterns.EMAIL_ADDRESS.matcher(emailText).matches()
        } else {
            true
        }
    }

    fun isPasswordValid(): Boolean {
        return if (emailText.isNotEmpty() && passwordText.isNotEmpty()) {
            passwordText.length > 5
        } else {
            true
        }
    }

    // This function is validation check result
    fun checkValidation(): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(emailText).matches() && passwordText.length > 5

    // Clearing User Filled Form for the security reasons.
    private fun clearActions() {
        emailText = ""
        passwordText = ""
    }

    // onValueChanged Changed Listeners
    fun onEmailChanged(value: String) {
        emailText = value
    }

    fun onPasswordChanged(value: String) {
        passwordText = value
    }

    init {
        checkConnectivity()
    }

    // Login use case control to the login state
    fun loginUser() = viewModelScope.launch {
        loginUseCase(emailText, passwordText).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _loginState.value = LoginState(user = result.data).also {
                        clearActions()
                    }
                }
                is Resource.Error -> {
                    _loginState.value = LoginState(error = result.message.toString())
                }
                is Resource.Loading -> {
                    _loginState.value = LoginState(isLoading = true)
                }
            }
        }
    }

    // Check user last state for UI
    fun controlUser() = viewModelScope.launch {
        loginStateUseCase().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _loginState.value = LoginState(user = result.data)
                }
                is Resource.Error -> {
                    _loginState.value = LoginState(error = result.message.toString())
                }
                is Resource.Loading -> {
                    // Is loading for Splash Screen
                    _isLoading.value = false
                }
            }
        }
    }

    // Sign out to the clear user memory
    fun signOut() = viewModelScope.launch {
        signOutUseCase().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _loginState.value = LoginState(user = null)
                }
                is Resource.Error -> {
                    _loginState.value = LoginState(error = result.message.toString())
                }
                is Resource.Loading -> {
                    _loginState.value = LoginState(isLoading = true)
                }
            }
        }
    }

    fun getUsers(uid: String? = _loginState.value.user?.uid) =
        getUsersUseCase(uid).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let { list ->
                        list.forEach { user ->
                            _usersState.add(user)
                        }
                    }
                }
                is Resource.Error -> {
                    _userState.value = UserState(error = result.message.toString())
                }
                is Resource.Loading -> {
                    _userState.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    fun getUser(uid: String? = _loginState.value.user?.uid) =
        getUserUseCase(uid).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _userState.value = UserState(user = result.data)
                }
                is Resource.Error -> {
                    _userState.value = UserState(error = result.data.toString())
                }
                is Resource.Loading -> {
                    _userState.value = UserState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)

    private fun checkConnectivity() = viewModelScope.launch {
        connectivityObserver.observe().collect {
            _connectivityState.value = it
        }
    }
}