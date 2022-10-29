package com.emirhan.socialapp.presentation.utils.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.emirhan.socialapp.presentation.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsContent(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    val commentValue = homeViewModel.commentValue
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Box(
            Modifier.fillMaxWidth()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Divider(
                    Modifier
                        .size(width = 50.dp, height = 4.5.dp)
                        .clip(CircleShape),
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 2.dp
                )
            }

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Blank Avatar",
                    modifier = Modifier
                        .padding(top = 5.dp, start = 25.dp)
                        .size(54.dp)
                        .clip(CircleShape)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground,
                            CircleShape
                        )
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )

                TextField(
                    value = commentValue.value,
                    onValueChange = { homeViewModel.commentValue(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp),
                    placeholder = {
                        Text(text = "Enter Comment")
                    }
                )
            }
        }
    }
}
