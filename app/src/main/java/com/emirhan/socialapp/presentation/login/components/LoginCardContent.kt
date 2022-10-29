@file:OptIn(ExperimentalMaterialApi::class)

package com.emirhan.socialapp.presentation.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emirhan.socialapp.core.Constants
import com.emirhan.socialapp.presentation.login.LoginViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
// Constants
import com.emirhan.socialapp.core.Constants.Companion.EMAIL_PLACEHOLDER
import com.emirhan.socialapp.core.Constants.Companion.FORGOT_PASSWORD_BUTTON
import com.emirhan.socialapp.core.Constants.Companion.SIGN_UP_BUTTON
import com.emirhan.socialapp.core.Constants.Companion.DESC_HIDE_PASSWORD
import com.emirhan.socialapp.core.Constants.Companion.DESC_SHOW_PASSWORD
import com.emirhan.socialapp.core.Constants.Companion.ERROR_EMAIL
import com.emirhan.socialapp.core.Constants.Companion.ERROR_PASSWORD
import com.emirhan.socialapp.core.Constants.Companion.LOG_OUT_BUTTON

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginCardContent(
    padding: PaddingValues,
    viewModel: LoginViewModel = hiltViewModel(),
    sheetState: ModalBottomSheetState,
) {
    // Check user logged state
    val loginState = viewModel.loginState.value

    LaunchedEffect(loginState) {
        viewModel.controlUser()
    }

    Column(
        modifier = Modifier
            .padding(padding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Control User State
        if (loginState.user != null) {
            // If the user logged in
            ProfileCardUI(
                viewModel = viewModel
            )
        } else {
            // If the user is not logged in
            LoginCardUI(
                registerSheetState = sheetState,
                viewModel = viewModel
            )
        }
    }
}


@Composable
fun ProfileCardUI(
    viewModel: LoginViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        TextButton(onClick = viewModel::signOut) {
            Text(text = LOG_OUT_BUTTON)
        }
    }
}

@Composable
fun LoginCardUI(
    viewModel: LoginViewModel = hiltViewModel(),
    registerSheetState: ModalBottomSheetState,
) {
    /*
    * Initialize Variables
    * Password Visibility, Sheet Hide/Show, ViewModel Communication
    */
    val emailText = viewModel.emailText
    val passwordText = viewModel.passwordText

    val passwordFocusRequester = remember { FocusRequester() }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    /*
    * E-Mail Text Field
    */
    LoginTextField(
        text = emailText,
        onValueChanged = viewModel::onEmailChanged,
        placeholder = EMAIL_PLACEHOLDER,
        modifier = Modifier
            .padding(top = 20.dp),
        isError = !viewModel.isEmailValid(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { passwordFocusRequester.requestFocus() }
        )
    )

    if (!viewModel.isEmailValid())
        Text(
            text = ERROR_EMAIL,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )

    Spacer(modifier = Modifier.height(10.dp))


    /*
    * Password Text Field
    */
    LoginTextField(
        text = passwordText,
        onValueChanged = viewModel::onPasswordChanged,
        placeholder = Constants.PASSWORD_PLACEHOLDER,
        modifier = Modifier.focusRequester(passwordFocusRequester),
        isError = !viewModel.isPasswordValid(),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            val description =
                if (passwordVisible) DESC_HIDE_PASSWORD else DESC_SHOW_PASSWORD
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, description)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { if (viewModel.checkValidation()) viewModel.loginUser() }
        )
    )

    if (!viewModel.isPasswordValid()) {
        Text(
            text = ERROR_PASSWORD,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall,
        )
    }
    Spacer(modifier = Modifier.height(10.dp))

    /*
    * Login Button Row
    */
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier.widthIn(min = 250.dp),
            onClick = viewModel::loginUser,
            enabled = viewModel.checkValidation(),
            shape = MaterialTheme.shapes.small,
        ) {
            Text(
                text = Constants.LOGIN_BUTTON,
            )
        }
    }

    /*
    * Login Card UI - Bottom Row
    */
    Row(
        Modifier
            .fillMaxSize()
            .padding(9.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        TextButton(
            onClick = {
                scope.launch {
                    if (registerSheetState.isVisible) {
                        registerSheetState.hide()
                    } else {
                        registerSheetState.show()
                    }
                }
            },
        ) {
            Text(
                text = SIGN_UP_BUTTON,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        TextButton(
            onClick = {},
        ) {
            Text(
                text = FORGOT_PASSWORD_BUTTON,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
