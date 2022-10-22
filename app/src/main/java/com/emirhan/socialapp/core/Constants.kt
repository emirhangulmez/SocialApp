
package com.emirhan.socialapp.core

class Constants {
    companion object {
        // Screens & Titles
        const val LOGIN_SCREEN = "Log-In"
        const val HOME_SCREEN = "Home"
        const val LOGIN_TITLE = "Profile"
        const val HOME_TITLE = "Home"

        // Login Screen
        // Placeholders
        const val EMAIL_PLACEHOLDER = "E-Mail"
        const val USERNAME_PLACEHOLDER = "Username"
        const val PASSWORD_PLACEHOLDER = "Password"
        // Error Messages
        const val ERROR_EMAIL = "Please write correct email!"
        const val ERROR_USERNAME = "In username must be at not special characters and not too long!"
        const val ERROR_PASSWORD = "Password must be at least 6 digits!"
        const val ALREADY_LOGGED_IN = "You are already logged in!"
        // Username Regex
        const val USERNAME_REGEX = "^[A-Za-z][A-Za-z0-9_]{5,12}$"
        const val DESC_HIDE_PASSWORD = "Hide Password"
        const val DESC_SHOW_PASSWORD = "Show Password"

        // Buttons
        const val LOGIN_BUTTON = "Log In"
        const val REGISTER_BUTTON = "Register"
        const val SIGN_UP_BUTTON = "Sign Up"
        const val FORGOT_PASSWORD_BUTTON = "Forgot Password?"
        const val LOG_OUT_BUTTON = "Log Out"

        // Firebase Constants
        // Firestore
        const val POSTS_COLLECTION = "posts"
        const val STORIES_COLLECTION = "stories"
        const val USERS_COLLECTION = "users"
        const val COMMENTS_COLLECTION = "comments"
        const val POSTDATE_FIELD = "postDate"
        // Storage
        const val IMAGES_PATH = "images/"
        const val STORIES_PATH = "stories/"
    }
}