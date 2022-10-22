package com.emirhan.socialapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.emirhan.socialapp.presentation.home.HomeScreen
import com.emirhan.socialapp.presentation.login.LoginScreen
import com.emirhan.socialapp.presentation.login.model.LoginState
import com.emirhan.socialapp.presentation.navigation.model.Screen.LoginScreen
import com.emirhan.socialapp.presentation.navigation.model.Screen.HomeScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    userState: LoginState?,
) {
    NavHost(
        navController = navController,
        startDestination = if (userState?.user != null) HomeScreen.route else LoginScreen.route
     ) {
        composable(
            route = LoginScreen.route
        ) {
                LoginScreen(
                    navigateToHomeScreen = {
                        navController.navigate(HomeScreen.route)
                    },
                    navController = navController
                )
        }
        composable(
            route = HomeScreen.route
        ) {
            HomeScreen(
                navigateToLoginScreen = {
                    navController.popBackStack()
                },
                navController = navController,
            )
        }

    }
}