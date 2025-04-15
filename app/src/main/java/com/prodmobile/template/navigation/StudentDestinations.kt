package com.prodmobile.template.navigation

import androidx.compose.material3.Scaffold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.util.navigateAndClean
import com.prodmobile.template.core.util.serializableType
import com.prodmobile.template.feature.account.components.OwnStudentProfileScreen
import com.prodmobile.template.feature.account.view_model.student.OwnStudentViewModel
import com.prodmobile.template.feature.favourite.components.FavouritesScreen
import com.prodmobile.template.feature.favourite.view_model.FavouritesViewModel
import com.prodmobile.template.feature.feed.FeedScreen
import com.prodmobile.template.feature.feed.view_model.FeedScreenViewModel
import com.prodmobile.template.feature.home.components.StudentHomeBottomBar
import com.prodmobile.template.feature.requests.components.StudentRequestsScreen
import com.prodmobile.template.feature.requests.view_model.StudentsRequestsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parameterSetOf
import kotlin.reflect.typeOf

fun NavGraphBuilder.studentDestinations(
    generalNavController: NavController,
) {
    composable<NavigationPath.StudentPath>(
        typeMap = mapOf(typeOf<AccountInfo>() to serializableType<AccountInfo>())
    ) { studentPathStack ->
        val info = studentPathStack.toRoute<NavigationPath.StudentPath>()
        val navController = rememberNavController()
        Scaffold(
            bottomBar = {
                StudentHomeBottomBar(navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = StudentHomeDestinations.Feed
            ) {
                composable<StudentHomeDestinations.Profile> {
                    val viewModel =
                        koinViewModel<OwnStudentViewModel>(parameters = {
                            parameterSetOf(
                                { generalNavController.navigateAndClean(NavigationPath.WelcomePath) }
                            )
                        })
                    OwnStudentProfileScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onEvent = { viewModel.onEvent(it) },
                        innerPadding = paddingValues,
                    )
                }
                composable<StudentHomeDestinations.Feed> {
                    val viewModel = koinInject<FeedScreenViewModel>()
                    FeedScreen(
                        state = viewModel.state.value,
                        onEvent = viewModel::onEvent,
                        navigateToMentorScreen = { accountId ->
                            generalNavController.navigate(
                                StudentHomeDestinations.MentorProfile(
                                    accountId
                                )
                            )
                        },
                        innerPadding = paddingValues
                    )
                    // Feed screen
                }
                composable<StudentHomeDestinations.Favourite> {
                    val viewModel = koinViewModel<FavouritesViewModel>()
                    FavouritesScreen(
                        {
                            generalNavController.navigate(StudentHomeDestinations.MentorProfile(it))
                        },
                        viewModel.state.collectAsStateWithLifecycle().value,
                        { viewModel.onEvent(it) },
                        paddingValues
                    )
                }
                composable<StudentHomeDestinations.MyRequests> {
                    val viewModel = koinViewModel<StudentsRequestsViewModel>()
                    StudentRequestsScreen(
                        onNavigationToProfile = {
                            generalNavController.navigate(StudentHomeDestinations.MentorProfile(it))
                        },
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        onEvent = { viewModel.onEvent(it) },
                        paddingValues = paddingValues
                    )
                    // My requests screen
                }
            }
        }
    }
}

@Serializable
sealed interface StudentHomeDestinations {
    @Serializable
    data object Profile : StudentHomeDestinations

    @Serializable
    data object Feed : StudentHomeDestinations

    @Serializable
    data object Favourite : StudentHomeDestinations

    @Serializable
    data object MyRequests : StudentHomeDestinations

    @Serializable
    data class MentorProfile(val userId: String) : StudentHomeDestinations
}