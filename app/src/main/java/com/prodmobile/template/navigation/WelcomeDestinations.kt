package com.prodmobile.template.navigation

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prodmobile.template.core.util.navigateAndClean
import com.prodmobile.template.core.util.navigateWithLifecycle
import com.prodmobile.template.feature.auth.RoleSelectionScreen
import com.prodmobile.template.feature.auth.mentor.MentorRegistrationScreen
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationViewModel
import com.prodmobile.template.feature.auth.student.StudentRegistrationScreen
import com.prodmobile.template.feature.auth.student.view_model.StudentRegistrationViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

fun NavGraphBuilder.welcomeDestinations(
    onNavigateToHomeScreen: (NavigationPath) -> Unit,
) {
    composable<NavigationPath.WelcomePath> {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = WelcomeDestinations.Empty,
        ) {
            composable<WelcomeDestinations.Mentor> {
                val viewModel = koinViewModel<MentorRegistrationViewModel>()
                MentorRegistrationScreen(
                    state = viewModel.state.collectAsStateWithLifecycle().value,
                    onEvent = viewModel::onEvent,
                    onNavigateToHomeScreen = { accountInfo ->
                        onNavigateToHomeScreen(NavigationPath.MentorPath(accountInfo))
                    }
                )
            }
            composable<WelcomeDestinations.Student> {
                val viewModel = koinViewModel<StudentRegistrationViewModel>()
                StudentRegistrationScreen(
                    state = viewModel.state.collectAsStateWithLifecycle().value,
                    onEvent = viewModel::onEvent,
                    onNavigateToHomeScreen = { accountInfo ->
                        onNavigateToHomeScreen(NavigationPath.StudentPath(accountInfo))
                    }
                )
            }
            composable<WelcomeDestinations.Empty> {
                RoleSelectionScreen(
                    onNavigationToMentorsRegistration = {
                        navigateWithLifecycle(navController) {
                            it.navigateAndClean(WelcomeDestinations.Mentor)
                        }
                    },
                    onNavigationToStudentRegistration = {
                        navigateWithLifecycle(navController) {
                            it.navigateAndClean(WelcomeDestinations.Student)
                        }
                    }
                )
            }
        }
    }
}

@Serializable
sealed interface WelcomeDestinations {
    @Serializable
    data object Mentor : WelcomeDestinations

    @Serializable
    data object Student : WelcomeDestinations

    @Serializable
    data object Empty : WelcomeDestinations
}