package com.prodmobile.template.feature.auth.student

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.models.UserRole
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.feature.auth.components.StudentNameAndImagePage
import com.prodmobile.template.feature.auth.components.StudentSkillsPage
import com.prodmobile.template.feature.auth.student.view_model.StudentRegistrationEvent
import com.prodmobile.template.feature.auth.student.view_model.StudentRegistrationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentRegistrationScreen(
    state: StudentRegistrationState,
    onEvent: (StudentRegistrationEvent) -> Unit,
    onNavigateToHomeScreen: (AccountInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(0) { 2 }

    LaunchedEffect(state.userRegistrationResult) {
        val userRegistrationResult = state.userRegistrationResult
        if (userRegistrationResult is ResponseResult.Success) {
            val accountInfo = AccountInfo(
                userRegistrationResult.data.id,
                UserRole.Student,
                userRegistrationResult.data.accessToken
            )
            onNavigateToHomeScreen(accountInfo)
        }
    }
    LaunchedEffect(state.userSignInResult) {
        val userSignInResult = state.userSignInResult ?: return@LaunchedEffect
        // valid credentials
        if (userSignInResult is ResponseResult.Success) {
            val accountInfo = AccountInfo(
                userSignInResult.data.id,
                UserRole.Student,
                userSignInResult.data.accessToken
            )
            onNavigateToHomeScreen(accountInfo)
        } else {
            println("DEBUG LOG: INVALID CREDENTIALS")
            // invalid credentials
            coroutine.launch {
                pagerState.animateScrollToPage(1)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            LinearProgressIndicator(
                                progress = {
                                    (pagerState.currentPage + 1).toFloat() / pagerState.pageCount.toFloat()
                                },
                                drawStopIndicator = {}
                            )
                            Spacer(Modifier.width(16.dp))
                            Text(
                                text = "${pagerState.currentPage + 1}/${pagerState.pageCount}"
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false,
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
            ) { page ->
                when (page) {
                    0 -> {
                        StudentNameAndImagePage(
                            state = state,
                            onEvent = {
                                onEvent(it)
                            },
                            onContinue = {
                                onEvent(StudentRegistrationEvent.SignIn)
                            },
                            pagerState.currentPage == 0
                        )
                    }

                    1 -> {
                        StudentSkillsPage(
                            onDataSubmitted = { submittedMentorSkills ->
                                onEvent(
                                    StudentRegistrationEvent.WantedSkillsUpdate(
                                        submittedMentorSkills
                                    )
                                )

                                onEvent(StudentRegistrationEvent.SignUp)
                            }
                        )
                    }
                }
            }
        }
    }
}