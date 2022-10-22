package com.emirhan.socialapp.presentation.home.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.emirhan.socialapp.R
import com.emirhan.socialapp.domain.model.Post
import com.emirhan.socialapp.domain.model.User
import com.emirhan.socialapp.domain.network.ConnectivityObserver
import com.valentinilk.shimmer.shimmer


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomePostCardSection(
    post: Post = Post(),
    sheetState: ModalBottomSheetState,
    status: ConnectivityObserver.Status? = null,
    user: User? = null
) {
    Card(
        if (post.pictureURL == "") {
            Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .shimmer()

        } else {
            Modifier
                .fillMaxWidth()
                .padding(30.dp)
       },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(25.dp)
    ) {
        // Top Row
        PostCardTopRow(
            Post = post,
            user = user
        )
        // Post Picture
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(post.pictureURL)
                .placeholder(R.drawable.placeholder)
                .scale(Scale.FIT)
                .crossfade(true)
                .build(),
            contentDescription = "Post Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(250.dp, 500.dp)
                .padding(10.dp)
                .clip(RoundedCornerShape(30.dp))
        )

        // Bottom Row
        PostCardBottomRow(
            sheetState = sheetState,
            status = status
        )
    }
}

//..




