package com.emirhan.socialapp.domain.model

import com.google.firebase.Timestamp

data class Comment(
    val postID: String = "",
    val date: Timestamp? = null,
    val comment: String = ""
)
