package com.prodmobile.template.navigation

import androidx.compose.material3.Scaffold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.util.navigateAndClean
import com.prodmobile.template.core.util.serializableType
import com.prodmobile.template.feature.account.components.OwnMentorProfileScreen
import com.prodmobile.template.feature.account.components.StudentProfileScreen
import com.prodmobile.template.feature.account.view_model.mentor.OwnMentorViewModel
import com.prodmobile.template.feature.account.view_model.student.StudentViewModel
import com.prodmobile.template.feature.home.components.MentorHomeBottomBar
import com.prodmobile.template.feature.requests.components.MentorRequestsScreen
import com.prodmobile.template.feature.requests.view_model.MentorsRequestsViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parameterSetOf
import kotlin.reflect.typeOf

fun NavGraphBuilder.mentorDestinations(generalNavController: NavHostController) {
    composable<NavigationPath.MentorPath>(
        typeMap = mapOf(typeOf<AccountInfo>() to serializableType<AccountInfo>())
    ) {
        val navController = rememberNavController()

        Scaffold(
            bottomBar = {
                MentorHomeBottomBar(navController)
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = MentorHomeDestinations.Profile
            ) {
                composable<MentorHomeDestinations.Requests> { _ ->
                    val viewModel = koinViewModel<MentorsRequestsViewModel>()
                    MentorRequestsScreen(
                        viewModel.state.collectAsStateWithLifecycle().value,
                        { userId ->
                            navController.navigate(MentorHomeDestinations.ViewStudentAccount(userId))
                        },
                        { viewModel.onEvent(it) },
                        paddingValues
                    )
                }
                composable<MentorHomeDestinations.Profile> {
                    val viewModel =
                        koinViewModel<OwnMentorViewModel>(parameters = {
                            parameterSetOf({
                                generalNavController.navigateAndClean(
                                    NavigationPath.WelcomePath
                                )
                            })
                        })
                    OwnMentorProfileScreen(
                        viewModel.state.collectAsStateWithLifecycle().value,
                        { viewModel.onEvent(it) },
                        paddingValues,
                    )
                }
                composable<MentorHomeDestinations.ViewStudentAccount> { navStack ->
                    val viewAccountInfo =
                        navStack.toRoute<MentorHomeDestinations.ViewStudentAccount>()
                    val viewModel = koinViewModel<StudentViewModel>(parameters = {
                        parameterSetOf(viewAccountInfo.studentId)
                    })
                    StudentProfileScreen(
                        state = viewModel.state.collectAsStateWithLifecycle().value,
                        innerPadding = paddingValues,
                    )
                }
            }
        }
    }
}

@Serializable
sealed interface MentorHomeDestinations {
    @Serializable
    data object Requests : MentorHomeDestinations

    @Serializable
    data object Profile : MentorHomeDestinations

    @Serializable
    data class ViewStudentAccount(val studentId: String) : MentorHomeDestinations
}