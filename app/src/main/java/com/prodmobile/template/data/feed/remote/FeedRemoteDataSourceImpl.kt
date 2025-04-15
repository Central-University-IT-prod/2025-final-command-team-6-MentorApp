package com.prodmobile.template.data.feed.remote

import com.prodmobile.template.core.http_client.safeCall
import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.TemplateApi
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class FeedRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val templateApi: TemplateApi,
) : FeedRemoteDataSource {
    override suspend fun getFeed(jwtToken: String): ResponseResult<Array<MentorInfoDto>, NetworkError> {
        return safeCall<Array<MentorInfoDto>> {
            httpClient.get(
                templateApi.url.appendPathSegments("student", "find").build()
            ) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun dislikeMentor(mentorId: String, jwtToken: String) {
        runCatching {
            httpClient.post(
                templateApi.url.appendPathSegments("student", "swipe_mentor").build()
            ) {
                bearerAuth(jwtToken)
                setBody(SwipeInfoDto(mentorId, "dislike"))
            }
        }
    }

    override suspend fun likeMentor(mentorId: String, jwtToken: String) {
        runCatching {
            httpClient.post(
                templateApi.url.appendPathSegments("student", "swipe_mentor").build()
            ) {
                bearerAuth(jwtToken)
                setBody(SwipeInfoDto(mentorId, "like"))
            }
        }
    }

    override suspend fun addToFavouriteMentor(mentorId: String, jwtToken: String) {
        runCatching {
            httpClient.post(
                templateApi.url.appendPathSegments("student", "swipe_mentor").build()
            ) {
                bearerAuth(jwtToken)
                setBody(SwipeInfoDto(mentorId, "favorites"))
            }
        }
    }
}

@Serializable
data class SwipeInfoDto(
    @SerialName("mentor_id")
    val mentorId: String,
    @SerialName("type")
    val action: String
)
