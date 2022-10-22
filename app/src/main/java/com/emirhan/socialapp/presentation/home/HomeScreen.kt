@file:Suppress("DEPRECATION")

package com.emirhan.socialapp.presentation.home

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.emirhan.socialapp.presentation.login.LoginViewModel
import com.emirhan.socialapp.presentation.navigation.model.Screen
import com.emirhan.socialapp.presentation.navigation.components.BottomNavigationBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.emirhan.socialapp.presentation.home.components.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import com.emirhan.socialapp.presentation.utils.sheets.CommentsContent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navigateToLoginScreen: () -> Unit,
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    val storiesState = homeViewModel.storiesState.value

    val postsState = homeViewModel.postState.value

    val status by loginViewModel.connectivityState.collectAsState()

    val usersState = loginViewModel.usersState

    ModalBottomSheetLayout(
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        sheetShape = MaterialTheme.shapes.extraLarge,
        sheetElevation = 25.dp,
        sheetContent = { CommentsContent(homeViewModel) },
        sheetBackgroundColor = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = Screen.HomeScreen.route
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            floatingActionButton = {
                FloatingActionButton(
                     shape = CircleShape,
                     containerColor = MaterialTheme.colorScheme.onBackground,
                     onClick = { navigateToLoginScreen() },
                 ) {
                    Icon(

                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add icon",
                        tint = MaterialTheme.colorScheme.background,
                    )
                }
            },
            backgroundColor = MaterialTheme.colorScheme.background
        )
        { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize().padding(paddingValues),
            ) {
                LazyColumn(
                    Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 40.dp),

                    ) {
                    item {
                        TopHomeBar(
                            username = loginViewModel.loginState.value.user?.displayName,
                            navigateToLoginScreen
                        )
                    }
                    storiesState.isLoading.let {
                        if (it) {
                            item {
                                HomeStoryCardSection(
                                    status = ConnectivityObserver.Status.Unavailable
                                )
                            }
                        }
                    }
                    storiesState.stories?.let {
                        item {
                            HomeStoryCardSection(
                                listStory = it,
                                status = status,

                            )
                        }
                    }
                    postsState.isLoading.let { isLoading ->
                        item {
                            if (isLoading) {
                                Row(
                                    Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        Modifier
                                            .size(100.dp)
                                            .padding(20.dp))
                                }
                            }
                        }
                    }
                    postsState.posts?.let {
                        usersState.let { user ->
                            if (status == ConnectivityObserver.Status.Available)
                                items(postsState.posts) { post ->
                                    LaunchedEffect(post) {
                                        loginViewModel.getUsers(post.userUID)
                                    }
                                    HomePostCardSection(
                                        post = post,
                                        sheetState = sheetState,
                                        status = status,
                                        user = user.firstOrNull { it?.uid == post.userUID }
                                    )
                                }
                            else
                                // Internet is not available!
                                // In that case is We showing in Shimmer Effect with Post Card.
                                items(3){
                                    HomePostCardSection(
                                        sheetState = sheetState,
                                        status = status
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}

// This will changed soon!
@Composable
fun requestPermission(
    homeViewModel: HomeViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) : ManagedActivityResultLauncher<String, Uri?> {

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
        LaunchedEffect(bitmap) {
            homeViewModel.createStory(
                Story(
                    pictureBitmap = bitmap.value,
                    userUID = loginViewModel.loginState.value.user?.uid ?: ""
                )
            )
        }
    }
    return launcher
}
