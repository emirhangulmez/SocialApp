package com.emirhan.socialapp.presentation.utils

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.*

/*
*
* Composable Utils
*
*/

@Composable
fun BlankAvatar(
    avatarSize: Dp = 54.dp
) {
    Icon(
        imageVector = Icons.Filled.Person,
        tint = MaterialTheme.colorScheme.onBackground,
        contentDescription = "Blank Avatar",
        modifier = Modifier
            .padding(5.dp)
            .size(avatarSize)
            .clip(CircleShape)
            .border(
                1.dp,
                MaterialTheme.colorScheme.onBackground,
                CircleShape
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )
}

@Composable
fun dateLabel(timestamp: Date, today: Date): String {
    return if (today.time - timestamp.time < 2 * DateUtils.MINUTE_IN_MILLIS) {
        "Just Now"
    } else {
        DateUtils.getRelativeTimeSpanString(
            timestamp.time,
            today.time,
            0,
            DateUtils.FORMAT_ABBREV_MONTH
        ).toString()
    }
}