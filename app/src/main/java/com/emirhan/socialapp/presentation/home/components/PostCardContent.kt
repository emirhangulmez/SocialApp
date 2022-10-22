package com.emirhan.socialapp.presentation.home.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.Comment
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.emirhan.socialapp.domain.model.Post
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.emirhan.socialapp.presentation.utils.BlankAvatar
import com.emirhan.socialapp.presentation.utils.dateLabel
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun PostCardTopRow(
    Post: Post,
    user: User? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        user?.let {
            if (it.avatarURL != "")
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.avatarURL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "User Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(5.dp)
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(
                            4.dp,
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            else
                BlankAvatar()
        }


        Box(Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                Text(
                    text = user?.displayName ?: "",
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 10.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
                val today = remember {
                    Date()
                }
                val dateText =
                    Post.postDate?.let { dateLabel(timestamp = it.toDate(), today = today) }
                Text(
                    text = dateText ?: "",
                    Modifier.padding(start = 10.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreHoriz,
                    contentDescription = "Post Menu"
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PostCardBottomRow(
    sheetState: ModalBottomSheetState,
    status: ConnectivityObserver.Status? = null
) {
    val scope = rememberCoroutineScope()
    var favoriteVisibility by rememberSaveable { mutableStateOf(false) }

    // Favorite Button
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 5.dp)
    ) {
        IconButton(
            onClick = { if (status == ConnectivityObserver.Status.Available) favoriteVisibility = !favoriteVisibility },
            modifier = Modifier.size(30.dp)
        ) {
            val targetColor by animateColorAsState(
                targetValue = if (favoriteVisibility) Color.Red else LocalContentColor.current,
                animationSpec = tween(666)
            )
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Heart",
                modifier = Modifier.size(30.dp),
                tint = targetColor
            )
        }

        // Comment Button
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = { if (status == ConnectivityObserver.Status.Available) scope.launch { if (sheetState.isVisible) sheetState.hide() else sheetState.show() } },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Comment,
                    contentDescription = "Heart",
                    modifier = Modifier
                        .size(30.dp)
                )
            }
        }
    }
}