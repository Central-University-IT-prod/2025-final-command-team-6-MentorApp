package com.prodmobile.template.data.accounts.mentor.remote

import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.accounts.student.remote.MentorReviewDto
import com.prodmobile.template.data.accounts.student.remote.StudentResponseDto
import com.prodmobile.template.feature.favourite.view_model.FavouriteElement
import kotlinx.coroutines.flow.Flow

interface MentorAccountRemoteDataSource {
    suspend fun getCurrentAccountInfo(jwtToken: String): ResponseResult<MentorInfoDto, NetworkError>
    suspend fun getStudentAccountById(mentorId: String, jwtToken: String): ResponseResult<StudentResponseDto, NetworkError>

    suspend fun observeMatchRequests(jwtToken: String): Flow<ResponseResult<List<MatchRequestFromMentorDto>, NetworkError>>
    fun getFavorites(jwtToken: String): List<FavouriteElement>
    suspend fun updateInfo(data: UpdatedMentorInfo, token: String)

    suspend fun declineMatchRequest(requestId: String, jwtToken: String)
    suspend fun acceptMatchRequest(requestId: String, jwtToken: String)
    suspend fun getOwnReviews(
        jwtToken: String
    ): ResponseResult<List<MentorReviewDto>, NetworkError>
}