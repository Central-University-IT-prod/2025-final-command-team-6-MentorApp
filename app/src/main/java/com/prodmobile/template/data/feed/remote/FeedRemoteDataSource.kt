package com.prodmobile.template.data.feed.remote

import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult

interface FeedRemoteDataSource {
    suspend fun getFeed(jwtToken: String): ResponseResult<Array<MentorInfoDto>, NetworkError>
    suspend fun dislikeMentor(mentorId: String, jwtToken: String)
    suspend fun addToFavouriteMentor(mentorId: String, jwtToken: String)
    suspend fun likeMentor(mentorId: String, jwtToken: String)
}