package com.raktaseva.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.raktaseva.app.presentation.ui.screens.auth.AuthScreen
import com.raktaseva.app.presentation.ui.screens.auth.ProfileSetupScreen
import com.raktaseva.app.presentation.ui.screens.dashboard.DashboardScreen
import com.raktaseva.app.presentation.ui.screens.request.RequestBloodScreen

@Composable
fun NavGraph(startDestination: String = "auth") {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("auth") { inclusive = true }
                    }
                },
                onNavigateToProfileSetup = {
                    navController.navigate("profile_setup") {
                        popUpTo("auth") { inclusive = true }
                    }
                }
            )
        }
        composable("profile_setup") {
            ProfileSetupScreen(
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("profile_setup") { inclusive = true }
                    }
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onNavigateToRequest = {
                    navController.navigate("request_blood")
                },
                onNavigateToAssistant = {
                    navController.navigate("assistant")
                }
            )
        }
        composable("request_blood") {
            RequestBloodScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("assistant") {
            GenAIAssistantScreen()
        }
    }
}
