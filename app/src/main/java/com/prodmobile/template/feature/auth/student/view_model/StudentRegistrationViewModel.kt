package com.prodmobile.template.feature.auth.student.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.models.AccountInfo
import com.prodmobile.template.core.models.UserRole
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.core.result.getOrNull
import com.prodmobile.template.core.result.onError
import com.prodmobile.template.core.result.onSuccess
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.auth.model.SignResponseDto
import com.prodmobile.template.data.auth.student.StudentAuthRemoteDataSource
import com.prodmobile.template.data.auth.student.model.StudentSignInData
import com.prodmobile.template.data.auth.student.model.StudentSignUpData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StudentRegistrationViewModel(
    private val localAccountInfo: AccountInfoDataSource,
    private val remoteAuth: StudentAuthRemoteDataSource
) : ViewModel() {
    private val _state = MutableStateFlow(StudentRegistrationState())
    val state: StateFlow<StudentRegistrationState>
        get() = _state

    fun onEvent(event: StudentRegistrationEvent) {
        when (event) {
            is StudentRegistrationEvent.AvatarUpdate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isImageLoading = true)
                    val bytes = event.contentResolver.openInputStream(event.imageUri)!!.readBytes()
                    _state.value = _state.value.copy(image = bytes, isImageLoading = false)
                }
            }

            is StudentRegistrationEvent.NameUpdate -> {
                _state.update {
                    it.copy(name = event.name)
                }
            }

            StudentRegistrationEvent.SignUp -> {
                _state.update {
                    it.copy(isLoading = true)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    with(_state.value) {
                        val request = remoteAuth.signUp(
                            StudentSignUpData(
                                name,
                                null,
                                wantedSkills,
                                image
                            )
                        )

                        if (request is ResponseResult.Error) {
                            _state.update {
                                it.copy(isLoading = false)
                            }

                            return@with
                        }

                        val (id, jwtToken) = (request.getOrNull() ?: return@with)
                        localAccountInfo.saveAccountInfo(
                            AccountInfo(
                                id,
                                UserRole.Student,
                                jwtToken,
                            )
                        )

                        _state.update {
                            it.copy(userRegistrationResult = request)
                        }
                    }
                }
            }

            is StudentRegistrationEvent.WantedSkillsUpdate -> {
                _state.update {
                    it.copy(wantedSkills = event.skills)
                }
            }

            StudentRegistrationEvent.SignIn -> {
                with(_state.value) {
                    viewModelScope.launch {
                        val request = remoteAuth.signIn(
                            StudentSignInData(name)
                        )
                        request.onSuccess { (id, jwtToken) ->
                            localAccountInfo.saveAccountInfo(
                                AccountInfo(
                                    id,
                                    UserRole.Student,
                                    jwtToken
                                )
                            )

                            _state.update {
                                it.copy(userSignInResult = request)
                            }
                        }.onError {
                            _state.update {
                                it.copy(userSignInResult = request)
                            }
                        }
                    }
                }
            }
        }
    }
}

data class StudentRegistrationState(
    val name: String = "",
    val wantedSkills: List<String> = emptyList(),
    val image: ByteArray? = null,
    val userSignInResult: ResponseResult<SignResponseDto, NetworkError>? = null,
    val userRegistrationResult: ResponseResult<SignResponseDto, NetworkError>? = null,
    val isLoading: Boolean = false,
    val isImageLoading: Boolean = false
)