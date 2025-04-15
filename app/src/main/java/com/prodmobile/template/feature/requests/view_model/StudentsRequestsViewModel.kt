package com.prodmobile.template.feature.requests.view_model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import com.prodmobile.template.feature.requests.model.RequestFromStudentModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class StudentsRequestsViewModel(
    private val accountInfo: AccountInfoDataSource,
    private val accountsRemoteDataSourceImpl: StudentAccountRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(StudentsRequestsScreenState())
    val state: StateFlow<StudentsRequestsScreenState>
        get() = _state
            .onStart {
                observingRequestsJob?.cancel()
                observeRequests()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(),
                StudentsRequestsScreenState()
            )

    private var observingRequestsJob: Job? = null

    private fun observeRequests() {
        observingRequestsJob?.cancel()
        observingRequestsJob = viewModelScope.launch(Dispatchers.IO) {
            val token = accountInfo.getAccountInfo()!!.jwtToken
            accountsRemoteDataSourceImpl
                .observeMatchRequests(token)
                .collect { response ->
                    when (response) {
                        is ResponseResult.Error -> {
                            _state.value = _state.value.copy(isLoading = false)
                        }

                        is ResponseResult.Success -> {
                            val processedData = response.data.map {
                                RequestFromStudentModel(
                                    it.id,
                                    it.mentor.id,
                                    it.mentor.fullName,
                                    it.mentor.avatarUrl,
                                    it.createdAt,
                                    it.mentor.contacts,
                                    it.mentor.skills,
                                    it.type
                                )
                            }
                            _state.value.requests.clear()
                            _state.value.requests.addAll(processedData)
                        }
                    }
                }
        }
    }

    fun onEvent(event: StudentsFavouritesScreenEvent) {
        when (event) {
            is StudentsFavouritesScreenEvent.DenyRequest -> {
                viewModelScope.launch {
                    val token = accountInfo.getAccountInfo()!!.jwtToken
                    accountsRemoteDataSourceImpl.deleteMatchRequest(event.requestId, token)
                    observeRequests()
                }
            }
        }
    }
}

data class StudentsRequestsScreenState(
    val requests: SnapshotStateList<RequestFromStudentModel> = mutableStateListOf(),
    var isLoading: Boolean = false,
)

sealed interface StudentsFavouritesScreenEvent {
    data class DenyRequest(val requestId: String) : StudentsFavouritesScreenEvent
}