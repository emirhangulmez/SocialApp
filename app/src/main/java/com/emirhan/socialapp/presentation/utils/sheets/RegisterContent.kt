package com.emirhan.socialapp.presentation.utils.sheets

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emirhan.socialapp.core.Constants
import com.emirhan.socialapp.core.Constants.Companion.DESC_HIDE_PASSWORD
import com.emirhan.socialapp.core.Constants.Companion.DESC_SHOW_PASSWORD
import com.emirhan.socialapp.core.Constants.Companion.EMAIL_PLACEHOLDER
import com.emirhan.socialapp.core.Constants.Companion.PASSWORD_PLACEHOLDER
import com.emirhan.socialapp.core.Constants.Companion.USERNAME_PLACEHOLDER
import com.emirhan.socialapp.presentation.login.LoginViewModel
import com.emirhan.socialapp.presentation.login.components.LoginTextField
import com.emirhan.socialapp.presentation.register.RegisterViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RegisterContent(
    viewModel: RegisterViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel(),
    sheetState: ModalBottomSheetState
) {
    val emailText = viewModel.emailText
    val passwordText = viewModel.passwordText
    val usernameText = viewModel.usernameText

    val state = viewModel.state.value

    val context = LocalContext.current

    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val usernameFocusRequester = remember {FocusRequester()}
    val passwordFocusRequester = remember {FocusRequester()}

    // Login automatically if user registered
    // Control error on state changed
    LaunchedEffect(state) {
        if (state.user != null) {
            sheetState.hide()
            loginViewModel.controlUser()
        }
        if (state.error.isNotBlank()){
            state.error.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Register Content
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.85f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        /*
        * E-Mail Text Field
        */
        LoginTextField(
            text = emailText,
            onValueChanged = viewModel::onEmailChanged,
            isError = !viewModel.isEmailValid(),
            placeholder = EMAIL_PLACEHOLDER,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { usernameFocusRequester.requestFocus() }
            )
        )

        if (!viewModel.isEmailValid()) {
            Text(
                text = Constants.ERROR_EMAIL,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        /*
        * Username Text Field
        */
        LoginTextField(
            text = usernameText,
            onValueChanged = viewModel::onUsernameChanged,
            placeholder = USERNAME_PLACEHOLDER,
            isError = !viewModel.isUsernameValid(),
            modifier = Modifier.focusRequester(usernameFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )

        if (!viewModel.isUsernameValid()) {
            Text(
                text = Constants.ERROR_USERNAME,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.height(25.dp))


        /*
        * Password Text Field
        */
        LoginTextField(
            text = passwordText,
            onValueChanged = viewModel::onPasswordChanged,
            placeholder = PASSWORD_PLACEHOLDER,
            isError = !viewModel.isPasswordValid(),
            modifier = Modifier.focusRequester(passwordFocusRequester),
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
                onDone = { if (viewModel.checkValidation()) viewModel.registerUser() }
            )
        )

        if (!viewModel.isPasswordValid()) {
            Text(
                text = Constants.ERROR_PASSWORD,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = viewModel::registerUser,
                modifier = Modifier.widthIn(250.dp),
                enabled = viewModel.checkValidation(),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = Constants.REGISTER_BUTTON
                )
            }
        }

    }
}

@Composable
fun BlankRegisterContent() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Constants.ALREADY_LOGGED_IN
        )
    }
}