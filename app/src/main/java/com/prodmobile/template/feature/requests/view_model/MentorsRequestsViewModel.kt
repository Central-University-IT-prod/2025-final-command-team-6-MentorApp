package com.prodmobile.template.feature.requests.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.mentor.remote.MentorAccountRemoteDataSource
import com.prodmobile.template.feature.requests.model.RequestFromMentorModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MentorsRequestsViewModel(
    private val accountInfo: AccountInfoDataSource,
    private val mentorAccountRemoteDataSource: MentorAccountRemoteDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(MentorsRequestsScreenState())
    val state: StateFlow<MentorsRequestsScreenState> = _state.asStateFlow()

    init {
        loadRequests()
    }

    private fun loadRequests() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            val token = accountInfo.getAccountInfo()?.jwtToken ?: return@launch
            mentorAccountRemoteDataSource
                .observeMatchRequests(token)
                .collect { response ->
                    when (response) {
                        is ResponseResult.Error -> {
                            _state.update { it.copy(isLoading = false) }
                        }

                        is ResponseResult.Success -> {
                            val requests = response.data.map {
                                RequestFromMentorModel(
                                    requestId = it.id,
                                    studentId = it.student.id,
                                    studentName = it.student.fullName,
                                    interests = it.student.interests,
                                    createdAt = it.createdAt,
                                    imageUrl = it.student.avatarUrl,
                                    status = it.type
                                )
                            }
                            _state.update {
                                it.copy(
                                    requests = requests,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    fun onEvent(event: MentorsFavouritesScreenEvent) {
        when (event) {
            is MentorsFavouritesScreenEvent.ApproveRequest -> {
                viewModelScope.launch {
                    mentorAccountRemoteDataSource.acceptMatchRequest(
                        event.requestId,
                        accountInfo.getAccountInfo()!!.jwtToken
                    )
                }
            }

            is MentorsFavouritesScreenEvent.DenyRequest -> {
                viewModelScope.launch {
                    mentorAccountRemoteDataSource.declineMatchRequest(
                        event.requestId,
                        accountInfo.getAccountInfo()!!.jwtToken
                    )
                }
            }
        }
    }
}

data class MentorsRequestsScreenState(
    val requests: List<RequestFromMentorModel> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface MentorsFavouritesScreenEvent {
    data class ApproveRequest(val requestId: String) : MentorsFavouritesScreenEvent
    data class DenyRequest(val requestId: String) : MentorsFavouritesScreenEvent
}