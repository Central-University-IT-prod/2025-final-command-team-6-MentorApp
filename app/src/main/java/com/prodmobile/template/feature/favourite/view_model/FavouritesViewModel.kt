package com.prodmobile.template.feature.favourite.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val accountInfo: AccountInfoDataSource,
    private val accountsRemoteDataSourceImpl: StudentAccountRemoteDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(FavouritesScreenState())
    val state = _state.onStart {
        loadData()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        FavouritesScreenState()
    )

    private fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = accountInfo.getAccountInfo()?.jwtToken ?: run {
                return@launch
            }

            _state.update { it.copy(isLoading = true) }

            accountsRemoteDataSourceImpl
                .observeFavorites(token)
                .collect { response ->
                    when (response) {
                        is ResponseResult.Error -> {
                            _state.value = _state.value.copy(isLoading = false)
                        }

                        is ResponseResult.Success -> {
                            _state.update {
                                it.copy(
                                    favourites = response.data.map { dto ->
                                        FavouriteElement(
                                            id = dto.id,
                                            name = dto.fullName,
                                            description = dto.description,
                                            imageUrl = dto.avatarUrl,
                                            contacts = dto.contacts,
                                            skills = dto.skills
                                        )
                                    },
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    init {
        loadData()
    }

    fun onEvent(event: FavouritesEvents) {
        when (event) {
            is FavouritesEvents.DeleteFromFavourite -> {
                val currentList = _state.value.favourites.toMutableList()

                _state.update {
                    it.copy(
                        favourites = currentList.apply {
                            removeAt(event.index)
                        }.toList()
                    )
                }

                viewModelScope.launch {
                    val token = accountInfo.getAccountInfo()!!.jwtToken
                    accountsRemoteDataSourceImpl.deleteFavorites(event.mentorId, token)
                }
            }
        }
    }
}

data class FavouritesScreenState(
    val favourites: List<FavouriteElement> = listOf(),
    val isLoading: Boolean = false,
)

data class FavouriteElement(
    val id: String,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val skills: List<String>,
    val contacts: List<MentorContact>
)