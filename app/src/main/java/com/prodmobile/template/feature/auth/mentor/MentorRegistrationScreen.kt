package com.prodmobile.template.feature.auth.mentor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.prodmobile.template.feature.auth.components.MentorContactPage
import com.prodmobile.template.feature.auth.components.MentorNameAndImagePage
import com.prodmobile.template.feature.auth.components.MentorSkillsPage
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationEvent
import com.prodmobile.template.feature.auth.mentor.view_model.MentorRegistrationState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MentorRegistrationScreen(
    state: MentorRegistrationState,
    onEvent: (MentorRegistrationEvent) -> Unit,
    onNavigateToHomeScreen: (AccountInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutine = rememberCoroutineScope()
    val pagerState = rememberPagerState(0) { 3 }

    LaunchedEffect(state.userRegistrationResult) {
        if (state.userRegistrationResult is ResponseResult.Success) {
            val accountInfo = AccountInfo(
                state.userRegistrationResult.data.id,
                UserRole.Mentor,
                state.userRegistrationResult.data.accessToken
            )
            onNavigateToHomeScreen(accountInfo)
        }
    }
    LaunchedEffect(state.userSignInResult) {
        val result = state.userSignInResult ?: return@LaunchedEffect
        if (result is ResponseResult.Success) {
            val accountInfo = AccountInfo(
                result.data.id,
                UserRole.Mentor,
                result.data.accessToken
            )
            onNavigateToHomeScreen(accountInfo)
        } else {
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
                                }
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (page) {
                        0 -> {
                            MentorNameAndImagePage(
                                state = state,
                                { onEvent(it) }
                            )
                        }

                        1 -> {
                            MentorSkillsPage(
                                onDataSubmitted = { submittedMentorSkills ->
                                    onEvent(
                                        MentorRegistrationEvent.SkillsUpdate(
                                            submittedMentorSkills
                                        )
                                    )

                                    coroutine.launch {
                                        pagerState.animateScrollToPage(2)
                                    }
                                }
                            )
                        }

                        2 -> {
                            MentorContactPage(
                                onDataSubmitted = { contactList ->
                                    onEvent(
                                        MentorRegistrationEvent.ContactsUpdate(
                                            contactList
                                        )
                                    )

                                    onEvent(MentorRegistrationEvent.SignUp)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}