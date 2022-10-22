package com.emirhan.socialapp.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Shapes

val Shapes = Shapes(
    small = RoundedCornerShape(9.dp),
    medium = RoundedCornerShape(15.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp)
)