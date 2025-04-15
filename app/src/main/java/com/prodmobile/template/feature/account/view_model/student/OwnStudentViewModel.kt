package com.prodmobile.template.feature.account.view_model.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.onSuccess
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OwnStudentViewModel(
    private val onNavigateToAuthScreen: () -> Unit,
    private val studentRemoteDataSource: StudentAccountRemoteDataSource,
    private val accountInfoDataSource: AccountInfoDataSource
) : ViewModel() {
    private val _state = MutableStateFlow<OwnStudentInfoState?>(null)
    val state: MutableStateFlow<OwnStudentInfoState?>
        get() = _state

    init {
        viewModelScope.launch {
            val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken

            val result =
                studentRemoteDataSource.getCurrentAccountInfo(jwtToken)
            result.onSuccess { responseToResult ->
                _state.update {
                    OwnStudentInfoState(
                        responseToResult.fullName,
                        responseToResult.avatarUrl,
                        responseToResult.interests
                    )
                }
            }
        }
    }

    fun onEvent(event: OwnStudentInfoEvent) {
        when (event) {
            OwnStudentInfoEvent.SignOut -> {
                viewModelScope.launch {
                    accountInfoDataSource.clearAccountInfo()
                    onNavigateToAuthScreen()
                }
            }
        }
    }
}
