package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.screens.*
import com.example.todoapp.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    val navController = rememberNavController()
                    val authViewModel = hiltViewModel<AuthViewModel>()
                    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

                    NavHost(
                        navController = navController,
                        startDestination = if (isLoggedIn) "main" else "login"
                    ) {
                        composable("main",
                            enterTransition = { slideInVertically(initialOffsetY = { -1000 }) }
                        ) { MainScreen(navController) }
                        composable("login",
                            enterTransition = { slideInHorizontally(initialOffsetX = { -1000 }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }) }
                        ) { LoginScreen(navController) }
                        composable("signup",
                            enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }) },
                            exitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }) }
                        ) { SignUpScreen(navController) }
                    }
                }
            }
        }
    }
}