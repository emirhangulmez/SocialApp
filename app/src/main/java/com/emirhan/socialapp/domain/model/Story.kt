
package com.emirhan.socialapp.domain.model

import android.graphics.Bitmap

data class Story(
    // This parameters are using in firebase
    var storyID: String? = "",
    val userUID: String? = "",
    var pictureURL: String? = "",
    // This parameter is not using in firebase.
    val pictureBitmap: Bitmap? = null
)