package com.emirhan.socialapp.presentation.navigation.model

import com.emirhan.socialapp.R
import com.emirhan.socialapp.core.Constants.Companion.HOME_SCREEN
import com.emirhan.socialapp.core.Constants.Companion.HOME_TITLE
import com.emirhan.socialapp.core.Constants.Companion.LOGIN_SCREEN
import com.emirhan.socialapp.core.Constants.Companion.LOGIN_TITLE

sealed class Screen(val route: String, var icon: Int, var title: String) {
    object HomeScreen : Screen(HOME_SCREEN, R.drawable.ic_nav_home, HOME_TITLE)
    object LoginScreen : Screen(LOGIN_SCREEN, R.drawable.ic_nav_profile, LOGIN_TITLE)
}