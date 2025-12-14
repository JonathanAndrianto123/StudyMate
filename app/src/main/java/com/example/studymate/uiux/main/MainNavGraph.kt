package com.example.studymate.uiux.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.studymate.uiux.auth.SignUp
import com.example.studymate.uiux.main.Routes
import com.example.studymate.uiux.materi.*
import com.example.studymate.uiux.session.*
import com.example.studymate.uiux.stats.StatsScreen
import com.example.studymate.uiux.profile.ProfileScreen
import com.example.studymate.uiux.main.WelcomeScreen

@Composable
fun MainNavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {

        composable(Routes.WELCOME) {
            WelcomeScreen(
                onSignUpClicked = {
                    navController.navigate(Routes.SIGN_UP)
                },
                onSignInClicked = {
                    navController.navigate(Routes.SIGN_UP) // sementara
                }
            )
        }

        composable(Routes.SIGN_UP) {
            SignUp(
                onSignUpClicked = {
                    navController.navigate(Routes.MATERI_LIST) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onSignInClicked = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MATERI_LIST) {
            ListMateriScreen(
                onAddClick = {
                    navController.navigate(Routes.ADD_MATERI)
                },
                onDetailClick = {
                    navController.navigate(Routes.DETAIL_MATERI)
                }
            )
        }

        composable(Routes.ADD_MATERI) {
            AddMateriScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.DETAIL_MATERI) {
            DetailMateriScreen(
                onStartTimer = {
                    navController.navigate(Routes.TIMER)
                }
            )
        }

        composable(Routes.TIMER) {
            TimerScreen(
                onStop = {
                    navController.navigate(Routes.SESSION_HISTORY)
                }
            )
        }

        composable(Routes.SESSION_HISTORY) {
            SessionHistoryScreen()
        }

        composable(Routes.STATS) {
            StatsScreen()
        }

        composable(Routes.PROFILE) {
            ProfileScreen(
                onEdit = {}
            )
        }
    }
}
