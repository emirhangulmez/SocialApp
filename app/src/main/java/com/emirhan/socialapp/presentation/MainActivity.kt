package com.emirhan.socialapp.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.emirhan.socialapp.presentation.login.LoginViewModel
import com.emirhan.socialapp.presentation.login.model.LoginState
import com.emirhan.socialapp.presentation.navigation.NavGraph
import com.emirhan.socialapp.presentation.theme.SocialAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private var userState: LoginState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        * Initialize Splash Screen and App Content
        */
        installSplashScreen().apply {
            loginViewModel.controlUser()
            userState = loginViewModel.loginState.value
            setKeepOnScreenCondition {
                loginViewModel.isLoading.value
            }
        }

        setContent {
            SocialApp()
        }

    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun SocialApp() {
        SocialAppTheme {
            // Initialize Status Bars
            StatusBar()
            // Initialize Whole App from NavGraph
            Surface(
                modifier = Modifier.fillMaxSize()
            ) {
                Scaffold {
                    NavGraph(
                        navController = rememberNavController(),
                        userState = userState,
                    )
                }
            }
        }
    }

    @Composable
    fun StatusBar() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        val color = MaterialTheme.colorScheme.surfaceVariant

        SideEffect {
            systemUiController.setSystemBarsColor(
                color = color,
                darkIcons = useDarkIcons
            )
        }
    }
}