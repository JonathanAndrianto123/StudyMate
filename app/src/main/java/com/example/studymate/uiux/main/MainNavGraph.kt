package com.example.studymate.uiux.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.studymate.data.local.StudymateDatabase
import com.example.studymate.data.repository.MateriRepository
import com.example.studymate.data.repository.UserRepository
import com.example.studymate.uiux.auth.*
import com.example.studymate.uiux.materi.*
import com.example.studymate.uiux.session.*
import com.example.studymate.uiux.stats.StatsScreen
import com.example.studymate.uiux.profile.ProfileScreen
import com.example.studymate.uiux.profile.ProfileViewModel
import com.example.studymate.uiux.profile.ProfileViewModelFactory
import com.example.studymate.uiux.profile.EditProfileScreen
import com.example.studymate.uiux.stats.StatsViewModel
import com.example.studymate.uiux.stats.StatsViewModelFactory

@Composable
fun MainNavGraph(navController: NavHostController) {

    val context = LocalContext.current

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
                    navController.navigate(Routes.SIGN_IN)
                }
            )
        }

        composable(Routes.SIGN_UP) {
            val db = remember { StudymateDatabase.getDatabase(context) }
            val userRepository = remember { UserRepository(db.userDao()) }
            val factory = remember { SignUpViewModelFactory(context, userRepository) }
            val viewModel: SignUpViewModel = viewModel(factory = factory)

            SignUp(
                viewModel = viewModel,
                onSignUpClicked = {
                    navController.navigate(Routes.MATERI_LIST) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onSignInClicked = {
                    navController.navigate(Routes.SIGN_IN) {
                        popUpTo(Routes.WELCOME)
                    }
                }
            )
        }

        composable(Routes.SIGN_IN) {
            val db = remember { StudymateDatabase.getDatabase(context) }
            val userRepository = remember { UserRepository(db.userDao()) }
            val factory = remember { SignInViewModelFactory(context, userRepository) }
            val viewModel: SignInViewModel = viewModel(factory = factory)

            SignInScreen(
                viewModel = viewModel,
                onSignInSuccess = {
                    navController.navigate(Routes.MATERI_LIST) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                },
                onSignUpClicked = {
                    navController.navigate(Routes.SIGN_UP) {
                        popUpTo(Routes.WELCOME)
                    }
                }
            )
        }

        composable(Routes.MATERI_LIST) {
            val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
            val userId = userPrefs.getCurrentUserId()
            
            val db = remember { StudymateDatabase.getDatabase(context) }
            val repository = remember { MateriRepository(db.materiDao()) }
            val factory = remember { MateriViewModelFactory(repository, userId) }
            val viewModel: MateriViewModel = viewModel(factory = factory)

            ListMateriScreen(
                viewModel = viewModel,
                onAddClick = {
                    navController.navigate(Routes.ADD_MATERI)
                },
                onDetailClick = { materiId ->
                    navController.navigate(Routes.detailMateri(materiId))
                },
                onProfileClick = {
                    navController.navigate(Routes.PROFILE)
                }
            )
        }

        composable(Routes.ADD_MATERI) {
            val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
            val userId = userPrefs.getCurrentUserId()
            
            val db = StudymateDatabase.getDatabase(context)
            val repository = MateriRepository(db.materiDao())
            val factory = MateriViewModelFactory(repository, userId)

            val materiViewModel: MateriViewModel = viewModel(factory = factory)
            AddMateriScreen(
                onBack = { navController.popBackStack() },
                viewModel = materiViewModel
            )
        }

        composable(
            route = Routes.DETAIL_MATERI,
            arguments = listOf(
                navArgument("materiId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
            val userId = userPrefs.getCurrentUserId()
            
            val materiId = backStackEntry.arguments!!.getInt("materiId")

            val db = remember { StudymateDatabase.getDatabase(context) }
            val repository = remember { MateriRepository(db.materiDao()) }
            val factory = remember { MateriViewModelFactory(repository, userId) }

            val materiViewModel: MateriViewModel = viewModel(factory = factory)

            DetailMateriScreen(
                materiId = materiId,
                viewModel = materiViewModel,
                onBack = {
                    navController.popBackStack()
                },
                onStartTimer = { materiId, materiName ->
                    navController.navigate(Routes.timer(materiId, materiName))
                }
            )
        }


        composable(
            route = Routes.TIMER,
            arguments = listOf(
                navArgument("materiId") { type = NavType.IntType },
                navArgument("materiName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
            val userId = userPrefs.getCurrentUserId()
            
            val materiId = backStackEntry.arguments!!.getInt("materiId")
            val materiName = backStackEntry.arguments!!.getString("materiName") ?: "Unknown"

            val db = remember { StudymateDatabase.getDatabase(context) }
            val studySessionRepository = remember { com.example.studymate.data.repository.StudySessionRepository(db.studySessionDao()) }
            val factory = remember { SessionViewModelFactory(context, studySessionRepository, userId) }

            val sessionViewModel: SessionViewModel = viewModel(factory = factory)

            TimerScreen(
                materiId = materiId,
                materiName = materiName,
                viewModel = sessionViewModel,
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.SESSION_HISTORY) {
            SessionHistoryScreen()
        }

        composable(Routes.STATS) {
            val userPrefs = remember { com.example.studymate.util.UserPreferences(context) }
            val userId = userPrefs.getCurrentUserId()
            
            val db = remember { StudymateDatabase.getDatabase(context) }
            val repository = remember { MateriRepository(db.materiDao()) }
            val factory = remember { StatsViewModelFactory(repository, userId) }
            val viewModel = viewModel<StatsViewModel>(factory = factory)

            StatsScreen(viewModel = viewModel)
        }

        composable(Routes.PROFILE) {
            val db = remember { StudymateDatabase.getDatabase(context) }
            val userRepository = remember { UserRepository(db.userDao()) }
            val factory = remember { ProfileViewModelFactory(context, userRepository) }
            val viewModel: ProfileViewModel = viewModel(factory = factory)
            
            ProfileScreen(
                viewModel = viewModel,
                onEdit = {
                    navController.navigate(Routes.EDIT_PROFILE)
                },
                onHomeClick = {
                    navController.navigate(Routes.MATERI_LIST) {
                        launchSingleTop = true
                        popUpTo(Routes.MATERI_LIST) { inclusive = true }
                    }
                },
                onProfileClick = { /* Already here */ }
            )
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onSave = {
                    navController.popBackStack()
                },
                onDiscard = {
                    navController.popBackStack()
                }
            )
        }
    }
}
