package com.prodmobile.template.feature.account.view_model.mentor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.core.result.onSuccess
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import com.prodmobile.template.feature.requests.model.RequestStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MentorViewModel(
    private val mentorId: String,
    private val studentRemoteDataSource: StudentAccountRemoteDataSource,
    private val accountInfoDataSource: AccountInfoDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow<MentorInfoState?>(null)
    val state: MutableStateFlow<MentorInfoState?>
        get() = _state

    init {
        viewModelScope.launch {
            val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
            val request = studentRemoteDataSource.getMentorAccountById(mentorId, jwtToken)

            if (request is ResponseResult.Success) {
                _state.update {
                    val modifyState = it?.copy() ?: MentorInfoState(mentorId)
                    modifyState.copy(
                        // this shouldn't be sent anywhere
                        id = mentorId,
                        fullName = request.data.fullName,
                        avatarUrl = request.data.avatarUrl,
                        mentorDescription = request.data.description,
                        contacts = request.data.contacts,
                        skills = request.data.skills,
                    )
                }
            }

            val request2 = studentRemoteDataSource.getMatchRequests(mentorId, jwtToken)
            request2.onSuccess { data ->
                val canAddReview = data.filter {
                    it.type == RequestStatus.Accepted
                }
                    .any { it2 ->
                        it2.mentor.id == mentorId
                    }
                _state.update {
                    val modifyState = it?.copy() ?: MentorInfoState(mentorId)
                    modifyState.copy(canMakeComments = canAddReview)
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
        val request2 = studentRemoteDataSource.getReviews(mentorId, jwtToken)

        if (request2 is ResponseResult.Success) {
            _state.update {
                val modifyState = it?.copy() ?: MentorInfoState(mentorId)
                modifyState.copy(reviews = request2.data.map { response ->
                    ReviewInfo(
                        response.student.fullName,
                        response.student.avatarUrl,
                        response.text
                    )
                })
            }
        }
    }
    fun onEvent(event: MentorInfoEvent) {
        when (event) {
            is MentorInfoEvent.SendMatchRequest -> {
                viewModelScope.launch {
                    studentRemoteDataSource.matchRequest(
                        mentorId,
                        accountInfoDataSource.getAccountInfo()!!.jwtToken
                    )
                }
            }

            is MentorInfoEvent.SendReview -> {
                viewModelScope.launch {
                    studentRemoteDataSource.sendReview(
                        mentorId,
                        event.reviewText,
                        accountInfoDataSource.getAccountInfo()!!.jwtToken
                    )
                }
            }
        }
    }
}
