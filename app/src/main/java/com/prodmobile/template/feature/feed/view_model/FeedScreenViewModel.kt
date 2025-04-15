package com.prodmobile.template.feature.feed.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.account_info.AccountInfoDataSource
import com.prodmobile.template.data.accounts.student.remote.StudentAccountRemoteDataSource
import com.prodmobile.template.data.feed.remote.FeedRemoteDataSource
import com.prodmobile.template.feature.account.view_model.mentor.MentorInfoState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedScreenViewModel(
    private val remote: StudentAccountRemoteDataSource,
    private val remoteFeedRemoteDataSource: FeedRemoteDataSource,
    private val accountInfoDataSource: AccountInfoDataSource,
) : ViewModel() {
    private val _state = mutableStateOf(
        FeedScreenState()
    )
    val state: MutableState<FeedScreenState>
        get() = _state

    init {
        loadAdditionalProfiles()
    }

    private fun loadAdditionalProfiles() {
        viewModelScope.launch {
            val (_, _, jwtToken) = accountInfoDataSource.getAccountInfo()!!
            val element = remoteFeedRemoteDataSource.getFeed(jwtToken)
            if (element is ResponseResult.Success) {
                val data = element.data.map {
                    MentorInfoState(
                        it.id,
                        it.fullName,
                        it.avatarUrl,
                        it.description,
                        it.contacts,
                        skills = it.skills
                    ) to it.id
                }
                val elementsToAdd = data.filter { !_state.value.feed.contains(it.first) }
                _state.value.feed.addAll(elementsToAdd.map { it.first })
                mentorIds.addAll(elementsToAdd.map { it.second })
            }
        }.invokeOnCompletion {
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    private val mentorIds = mutableListOf<String>()
    fun onEvent(event: FeedScreenEvent) {
        when (event) {
            is FeedScreenEvent.Like -> {
                val currentIndex = _state.value.currentIndex
                val mentorId = mentorIds.removeAt(0)
                _state.value.feed[currentIndex] = null
                _state.value.currentIndex++

                viewModelScope.launch(Dispatchers.IO) {
                    val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
                    remoteFeedRemoteDataSource.likeMentor(mentorId, jwtToken)
                    loadAdditionalProfiles()
                }
            }

            FeedScreenEvent.Dislike -> {
                val currentIndex = _state.value.currentIndex
                val mentorId = mentorIds.removeAt(0)
                _state.value.feed[currentIndex] = null
                _state.value.currentIndex++

                viewModelScope.launch(Dispatchers.IO) {
                    val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
                    remoteFeedRemoteDataSource.dislikeMentor(mentorId, jwtToken)
                    loadAdditionalProfiles()
                }
            }

            FeedScreenEvent.Favorite -> {
                val currentIndex = _state.value.currentIndex
                val mentorId = mentorIds.removeAt(0)
                _state.value.feed[currentIndex] = null
                _state.value.currentIndex++

                viewModelScope.launch(Dispatchers.IO) {
                    val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
                    remoteFeedRemoteDataSource.addToFavouriteMentor(mentorId, jwtToken)
                    loadAdditionalProfiles()
                }
            }

            FeedScreenEvent.RemoveFromFavourite -> {
                val currentIndex = _state.value.currentIndex
                val mentorId = mentorIds[0]
                _state.value.feed[currentIndex] =
                    _state.value.feed[currentIndex]!!.copy(isFavorite = false)

                viewModelScope.launch(Dispatchers.IO) {
                    val jwtToken = accountInfoDataSource.getAccountInfo()!!.jwtToken
                    remote.deleteFavorites(mentorId, jwtToken)
                    loadAdditionalProfiles()
                }
            }
        }
    }
}

data class FeedScreenState(
    var currentIndex: Int = 0,
    val feed: SnapshotStateList<MentorInfoState?> = mutableStateListOf(),
    val isLoading: Boolean = true,
)
