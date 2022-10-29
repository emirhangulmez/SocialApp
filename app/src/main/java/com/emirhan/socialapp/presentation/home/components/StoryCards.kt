package com.emirhan.socialapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.emirhan.socialapp.R
import com.emirhan.socialapp.domain.model.Story
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.emirhan.socialapp.presentation.home.requestPermission
import com.emirhan.socialapp.presentation.login.LoginViewModel
import com.emirhan.socialapp.presentation.utils.BlankAvatar
import com.valentinilk.shimmer.shimmer


@Composable
fun HomeStoryCardSection(
    viewModel: LoginViewModel = hiltViewModel(),
    listStory: List<Story> = listOf(),
    status: ConnectivityObserver.Status? = null
) {
    LaunchedEffect(viewModel.loginState) {
        viewModel.controlUser()
    }

    val loginState by viewModel.loginState

    val usersState = viewModel.usersState

    val requestPermission = requestPermission()

    LazyRow(
        if (status == ConnectivityObserver.Status.Available) {
            Modifier.fillMaxWidth()
        } else {
            Modifier
                .fillMaxWidth()
                .shimmer()
        },
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .width(200.dp)
                    .height(350.dp)
                    .padding(20.dp)
                    .clickable {
                        if (status == ConnectivityObserver.Status.Available && loginState.user != null)
                            requestPermission.launch("image/*")
                    },
                shape = MaterialTheme.shapes.large,
            ) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add Story"
                )
            }
        }
        if (status == ConnectivityObserver.Status.Available) {
            items(listStory) { story ->
                LaunchedEffect(story) {
                    viewModel.getUsers(story.userUID)
                }
                StoryCards(
                    story = story,
                    user = usersState.firstOrNull { it?.uid == story.userUID }
                )
            }
        } else {
            items(2) {
                StoryCards()
            }
        }

    }
}

@Composable
fun StoryCards(
    story: Story = Story(),
    user: User? = null
) {
    // Scrim Feature
    var sizeImage by remember { mutableStateOf(IntSize.Zero) }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color.Transparent, Color.Black),
        startY = sizeImage.height.toFloat() / 3,  // 1/3
        endY = sizeImage.height.toFloat()
    )

    Card(
        modifier = Modifier
            .size(200.dp, 350.dp)
            .padding(20.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Story Picture
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(story.pictureURL)
                    .crossfade(true)
                    .build(),
                contentDescription = "Story Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    // Bottom Scrim
                    .onGloballyPositioned {
                        sizeImage = it.size
                    })
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(gradient)
            )
            // User Avatar
            user.let {
                if (it != null && it.avatarURL != "")
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(it.avatarURL)
                            .placeholder(R.drawable.placeholder)
                            .crossfade(true)
                            .build(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .border(
                                4.dp,
                                MaterialTheme.colorScheme.primary,
                                CircleShape
                            )
                    )
                else
                    BlankAvatar(36.dp)


                Text(
                    text = it?.displayName ?: "",
                    color = Color.White,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.BottomStart)
                )
            }
        }
    }
}
