package com.prodmobile.template.feature.app

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.models.UserRole
import com.prodmobile.template.core.util.navigateWithLifecycle
import com.prodmobile.template.feature.account.components.MentorProfileScreen
import com.prodmobile.template.feature.account.view_model.mentor.MentorViewModel
import com.prodmobile.template.navigation.NavigationPath
import com.prodmobile.template.navigation.StudentHomeDestinations
import com.prodmobile.template.navigation.mentorDestinations
import com.prodmobile.template.navigation.studentDestinations
import com.prodmobile.template.navigation.welcomeDestinations
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf

@Composable
fun AppScreen(
    accountInfo: AccountInfo?,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = when (accountInfo?.userRole) {
            null, UserRole.Unauthorized -> NavigationPath.WelcomePath
            UserRole.Mentor -> NavigationPath.MentorPath(accountInfo)
            UserRole.Student -> NavigationPath.StudentPath(accountInfo)
        },
    ) {
        welcomeDestinations(
            onNavigateToHomeScreen = {
                navController.navigate(it)
            }
        )
        mentorDestinations(
            generalNavController = navController
        )

        studentDestinations(
            generalNavController = navController
        )

        composable<StudentHomeDestinations.MentorProfile> {
            val data = it.toRoute<StudentHomeDestinations.MentorProfile>()
            val viewModel =
                koinViewModel<MentorViewModel>(parameters = {
                    parameterSetOf(
                        data.userId,
                    )
                })
            MentorProfileScreen(
                state = viewModel.state.collectAsStateWithLifecycle().value,
                onEvent = viewModel::onEvent,
                onBack = {
                    navigateWithLifecycle(navController) { currentController ->
                        currentController.popBackStack()
                    }
                }
            )
        }
    }
}