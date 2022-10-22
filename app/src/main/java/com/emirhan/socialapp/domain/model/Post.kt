
package com.emirhan.socialapp.domain.model

import com.google.firebase.Timestamp

data class Post(
    val postID : String = "",
    val postDate : Timestamp? = null,
    val pictureURL : String = "",
    val userUID : String = "",
    val likeCount : String = ""
)
