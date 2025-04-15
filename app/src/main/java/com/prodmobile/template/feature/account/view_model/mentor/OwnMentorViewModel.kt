package com.prodmobile.template.feature.account.view_model.mentor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.mentor.remote.MentorAccountRemoteDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class OwnMentorViewModel(
    val onNavigateToAuthScreen: () -> Unit,
    private val mentorRemoteDataSource: MentorAccountRemoteDataSource,
    private val accountInfoDataSource: AccountInfoDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow<MentorInfoState?>(null)
    val state: MutableStateFlow<MentorInfoState?>
        get() = _state

    init {
        viewModelScope.launch {
            val (accountId, _, jwtToken) = accountInfoDataSource.getAccountInfo()!!
            val request = mentorRemoteDataSource.getCurrentAccountInfo(
                jwtToken
            )

            if (request is ResponseResult.Success) {
                _state.update {
                    val modifyState = it?.copy() ?: MentorInfoState("")
                    modifyState.copy(
                        id = accountId,
                        fullName = request.data.fullName,
                        avatarUrl = request.data.avatarUrl,
                        contacts = request.data.contacts,
                        skills = request.data.skills,
                        mentorDescription = request.data.description,
                    )
                }
            }
            while (true) {
                loadComments()
                delay(1.seconds)
            }
        }
    }

    private suspend fun loadComments() {
        val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
        val request = mentorRemoteDataSource.getOwnReviews(
            jwtToken
        )

        if (request is ResponseResult.Success) {
            _state.update {
                val modifyState = it?.copy() ?: MentorInfoState("")
                modifyState.copy(reviews = request.data.map { response ->
                    ReviewInfo(
                        response.student.fullName,
                        response.student.avatarUrl,
                        response.text
                    )
                })
            }
        }
    }

    fun onEvent(event: OwnMentorInfoEvent) {
        when (event) {
            OwnMentorInfoEvent.SignOut -> {
                viewModelScope.launch {
                    accountInfoDataSource.clearAccountInfo()
                    onNavigateToAuthScreen()
                }
            }
        }
    }
}
