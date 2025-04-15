package com.prodmobile.template.data.accounts.student.remote

import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import kotlinx.coroutines.flow.Flow

interface StudentAccountRemoteDataSource {
    suspend fun getCurrentAccountInfo(jwtToken: String): ResponseResult<StudentResponseDto, NetworkError>
    suspend fun getMentorAccountById(
        mentorId: String,
        jwtToken: String
    ): ResponseResult<MentorInfoDto, NetworkError>

    suspend fun matchRequest(mentorId: String, jwtToken: String)

    suspend fun updateInfo(data: UpdatedUserInfo, token: String)
    suspend fun deleteFavorites(mentorId: String, jwtToken: String)
    suspend fun deleteMatchRequest(requestId: String, jwtToken: String)

    suspend fun getMatchRequests(mentorId: String, jwtToken: String  ): ResponseResult<List<MatchRequestFromStudentDto>, NetworkError>
    fun observeMatchRequests(jwtToken: String): Flow<ResponseResult<List<MatchRequestFromStudentDto>, NetworkError>>
    suspend fun observeFavorites(jwtToken: String): Flow<ResponseResult<List<MentorInfoDto>, NetworkError>>

    suspend fun sendReview(mentorId: String, reviewText: String, jwtToken: String)
    suspend fun getReviews(
        mentorId: String,
        jwtToken: String
    ): ResponseResult<List<MentorReviewDto>, NetworkError>
}