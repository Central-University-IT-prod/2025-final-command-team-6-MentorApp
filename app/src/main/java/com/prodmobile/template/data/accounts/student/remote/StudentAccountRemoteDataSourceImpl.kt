package com.prodmobile.template.data.accounts.student.remote

import com.prodmobile.template.core.http_client.safeCall
import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.TemplateApi
import com.prodmobile.template.feature.requests.model.RequestStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

class StudentAccountRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val templateApi: TemplateApi
) : StudentAccountRemoteDataSource {
    override suspend fun getCurrentAccountInfo(
        jwtToken: String
    ): ResponseResult<StudentResponseDto, NetworkError> {
        return safeCall<StudentResponseDto> {
            val url = templateApi.url
                .appendPathSegments("student", "me")
                .build()
            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun getMentorAccountById(
        mentorId: String,
        jwtToken: String
    ): ResponseResult<MentorInfoDto, NetworkError> {
        return safeCall<MentorInfoDto> {
            val url = templateApi.url
                .appendPathSegments("mentor", mentorId)
                .build()

            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun matchRequest(mentorId: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("student", "request")
                .build()
            httpClient.post(url) {
                setBody(MentorIdDto(mentorId))
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun sendReview(mentorId: String, reviewText: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("review", "")
                .build()
            httpClient.post(url) {
                setBody(ReviewDto(mentorId, reviewText, 1))
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun getReviews(
        mentorId: String, jwtToken: String
    ): ResponseResult<List<MentorReviewDto>, NetworkError> {
        return safeCall<List<MentorReviewDto>> {
            val url = templateApi.url
                .appendPathSegments("review", mentorId)
                .build()
            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun deleteMatchRequest(requestId: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("student", "request", requestId)
                .build()
            httpClient.delete(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun getMatchRequests(mentorId: String, jwtToken: String): ResponseResult<List<MatchRequestFromStudentDto>, NetworkError> {
        return safeCall<List<MatchRequestFromStudentDto>> {
            val url = templateApi.url
                .appendPathSegments("student", "request")
                .build()
            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun observeFavorites(
        jwtToken: String
    ): Flow<ResponseResult<List<MentorInfoDto>, NetworkError>> {
        return flow {
            runCatching {
                while (true) {
                    val result = safeCall<List<MentorInfoDto>> {
                        val url = templateApi.url
                            .appendPathSegments("student", "favorite")
                            .build()
                        httpClient.get(url) {
                            bearerAuth(jwtToken)
                        }
                    }
                    emit(result)
                    delay(1000)
                }
            }.onFailure {
            }
        }
    }

    override suspend fun deleteFavorites(mentorId: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("student", "favorite", mentorId)
                .build()
            httpClient.delete(url) {
                bearerAuth(jwtToken)
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    override fun observeMatchRequests(jwtToken: String): Flow<ResponseResult<List<MatchRequestFromStudentDto>, NetworkError>> {
        return flow {
            runCatching {
                while (true) {
                    val result = safeCall<List<MatchRequestFromStudentDto>> {
                        val url = templateApi.url
                            .appendPathSegments("student", "request")
                            .build()
                        httpClient.get(url) {
                            bearerAuth(jwtToken)
                        }
                    }
                    emit(result)
                    delay(1000)
                }
            }.onFailure {
            }
        }
    }

    override suspend fun updateInfo(data: UpdatedUserInfo, token: String) {
        val url = templateApi.url
            .appendPathSegments("student")
            .build()
        httpClient.put(url) {
            bearerAuth(token)
            setBody(data)
        }
    }
}

@Serializable
data class UpdatedUserInfo(
    val age: Long,
    val interests: List<String>
)

@Serializable
data class StudentResponseDto(
    val id: String,
    @SerialName("full_name")
    val fullName: String,
    val age: Long?,
    val interests: List<String>,
    @SerialName("avatar_url")
    val avatarUrl: String?,
    val description: String?
)

@Serializable
data class MentorIdDto(
    @SerialName("mentor_id")
    val id: String
)

@Serializable
data class MatchRequestFromStudentDto(
    val id: String,
    val type: RequestStatus,
    @SerialName("created_at")
    val createdAt: String,
    val mentor: MentorInfoDto
)

@Serializable
data class ReviewDto(
    @SerialName("mentor_id")
    val mentorId: String,
    val text: String,
    val rate: Long
)

@Serializable
data class MentorReviewDto(
    val mentor: MentorInfoDto,
    val student: StudentResponseDto,
    val text: String,
    val rate: Long
)