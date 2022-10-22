package com.emirhan.socialapp.presentation.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.emirhan.socialapp.presentation.login.components.LoginContent
import com.emirhan.socialapp.presentation.utils.sheets.RegisterContent
import com.emirhan.socialapp.presentation.navigation.model.Screen
import com.emirhan.socialapp.presentation.navigation.components.BottomNavigationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.emirhan.socialapp.presentation.utils.BlankAvatar
import com.emirhan.socialapp.presentation.utils.sheets.BlankRegisterContent

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun LoginScreen(
    navController: NavController,
    navigateToHomeScreen: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    ) {

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden, skipHalfExpanded = true)

    val loginState = viewModel.loginState.value

    val context = LocalContext.current

    val userState = viewModel.userState

    LaunchedEffect(loginState, userState) {
            loginState.error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }

            loginState.user?.let { user ->
                viewModel.getUser(user.uid)
            }
    }

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        sheetShape = MaterialTheme.shapes.extraLarge,
        sheetElevation = 25.dp,
        sheetContent = {
            if (viewModel.loginState.value.user == null)
                RegisterContent(sheetState = sheetState)
            else
              BlankRegisterContent()
           },
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
    ) {
        Scaffold(
            backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = Screen.LoginScreen.route
                )
            },
        ) { padding ->
            Column(
                Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                loginState.user?.let {
                    userState.value.user?.let { user ->
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(user.avatarURL)
                                .crossfade(true)
                                .scale(Scale.FILL)
                                .build(),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .padding(5.dp)
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        )
                    }
                    if (userState.value.user?.avatarURL == null) {
                        Box(modifier = Modifier.size(90.dp)) {
                            BlankAvatar(
                                avatarSize = 90.dp
                            )
                            Row(
                                Modifier.size(120.dp),
                                verticalAlignment = Alignment.Bottom,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.PhotoCamera,
                                    tint = MaterialTheme.colorScheme.onBackground,
                                    contentDescription = "Edit Profile Photo",
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(2.dp)
                                        .clickable { viewModel.getUsers() }
                                )
                            }
                        }
                    }
                }
                if (loginState.user == null) {
                    Icon(
                        imageVector = Icons.Filled.AccountBox,
                        modifier = Modifier
                            .size(128.dp)
                            .padding(top = 24.dp),
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Person"
                    )
                }
                        LoginContent(
                            padding = padding,
                            navigateToHomeScreen = navigateToHomeScreen,
                            sheetState = sheetState,
                            viewModel = viewModel
                        )
                    }
            }
        }
    }


