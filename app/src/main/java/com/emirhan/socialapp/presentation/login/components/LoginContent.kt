package com.emirhan.socialapp.presentation.login.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emirhan.socialapp.presentation.login.LoginViewModel
import androidx.compose.material3.CircularProgressIndicator

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LoginContent(
    padding: PaddingValues,
    navigateToHomeScreen: () -> Unit,
    sheetState: ModalBottomSheetState,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.loginState.value
    if (state.isLoading) {
        CircularProgressIndicator(Modifier.padding(10.dp))
    } else {
        Card(
            Modifier.padding(top = 20.dp),
            shape = MaterialTheme.shapes.extraLarge,
            elevation = 15.dp,
            backgroundColor = MaterialTheme.colorScheme.background
        ) {
            LoginCardContent(
                padding = padding,
                sheetState = sheetState,
            )
        }
    }
}

@Composable
fun LoginTextField(
    text: String,
    onValueChanged: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    TextField(
        modifier = modifier,
        value = text,
        shape = MaterialTheme.shapes.small,
        isError = isError,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            placeholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            textColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        keyboardOptions = keyboardOptions,
        onValueChange = onValueChanged,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardActions = keyboardActions,
        placeholder = {
            Text(
                text = placeholder
            )

        }
    )
}

