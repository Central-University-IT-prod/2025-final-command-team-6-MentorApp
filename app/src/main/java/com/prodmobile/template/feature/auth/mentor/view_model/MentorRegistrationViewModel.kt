package com.prodmobile.template.feature.auth.mentor.view_model

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
import com.prodmobile.template.data.auth.mentor.MentorAuthRemoteDataSource
import com.prodmobile.template.data.auth.mentor.model.MentorSignInData
import com.prodmobile.template.data.auth.mentor.model.MentorSignUpData
import com.prodmobile.template.data.auth.model.SignResponseDto
import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MentorRegistrationViewModel(
    private val localAccountInfo: AccountInfoDataSource,
    private val mentorAuthDataSource: MentorAuthRemoteDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(MentorRegistrationState())
    val state: StateFlow<MentorRegistrationState>
        get() = _state

    fun onEvent(event: MentorRegistrationEvent) {
        when (event) {
            is MentorRegistrationEvent.AboutUpdate -> {
                _state.value = _state.value.copy(about = event.about)
            }

            is MentorRegistrationEvent.AvatarUpdate -> {
                viewModelScope.launch(Dispatchers.IO) {
                    _state.value = _state.value.copy(isImageLoading = true)
                    val bytes = event.contentResolver.openInputStream(event.imageUri)!!.readBytes()
                    _state.value = _state.value.copy(image = bytes, isImageLoading = false)
                }
            }

            is MentorRegistrationEvent.ContactsUpdate -> {
                _state.value = _state.value.copy(contacts = event.contacts)
            }

            is MentorRegistrationEvent.NameUpdate -> {
                _state.value = _state.value.copy(name = event.name)
            }

            MentorRegistrationEvent.SignUp -> {
                _state.update {
                    it.copy(isLoading = true)
                }

                CoroutineScope(Dispatchers.IO).launch {
                    with(_state.value) {
                        val request = mentorAuthDataSource.signUp(
                            MentorSignUpData(
                                name,
                                about?.ifEmpty { null },
                                contacts,
                                skills,
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
                        val accountInfo = AccountInfo(
                            id,
                            UserRole.Mentor,
                            jwtToken
                        )
                        localAccountInfo.saveAccountInfo(accountInfo)

                        _state.update {
                            it.copy(userRegistrationResult = request)
                        }
                    }
                }
            }

            is MentorRegistrationEvent.SkillsUpdate -> {
                _state.value = _state.value.copy(skills = event.skills)
            }

            MentorRegistrationEvent.SignIn -> {
                with(_state.value) {
                    viewModelScope.launch {
                        val request = mentorAuthDataSource.signIn(
                            MentorSignInData(name)
                        )
                        request.onSuccess { (id, jwtToken) ->
                            localAccountInfo.saveAccountInfo(
                                AccountInfo(
                                    id,
                                    UserRole.Mentor,
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

data class MentorRegistrationState(
    val name: String = "",
    val skills: List<String> = listOf(),
    val about: String? = null,
    val contacts: List<MentorContact> = listOf(),
    val mentorImageUri: String? = null,
    val isLoading: Boolean = false,
    val userRegistrationResult: ResponseResult<SignResponseDto, NetworkError>? = null,
    val userSignInResult: ResponseResult<SignResponseDto, NetworkError>? = null,
    val image: ByteArray? = null,
    val isImageLoading: Boolean = false
)