package com.emirhan.socialapp.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.emirhan.socialapp.presentation.utils.BlankAvatar

@Composable
fun TopHomeBar(
    username: String?,
    navigateToLoginScreen: () -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(20.dp)) {
        // Notification Icon
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = {
                if (username == null) {
                    navigateToLoginScreen()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Notifications",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(5.dp)
                )
            }
        }
        // Texts, user avatar and info about can be register or login
        Row(Modifier.fillMaxWidth()) {
            BlankAvatar()
            Spacer(Modifier.width(10.dp))
            Column(Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(Modifier.fillMaxWidth()) {

                        Text(
                            text = "Hello",
                            modifier = Modifier.padding(top = 15.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.width(3.dp))

                        Text(
                            text = "${username ?: "User"} \uD83D\uDC4B",
                            modifier = Modifier.padding(top = 15.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
                if (username == null) {
                    Text(
                        text = "Please login or register!",
                        modifier = Modifier.clickable { navigateToLoginScreen() },
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}