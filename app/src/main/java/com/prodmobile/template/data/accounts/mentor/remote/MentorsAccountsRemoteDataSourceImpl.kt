package com.prodmobile.template.data.accounts.mentor.remote

import com.prodmobile.template.core.http_client.safeCall
import com.prodmobile.template.core.models.MentorInfoDto
import com.prodmobile.template.core.models.MentorVerdictDto
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.TemplateApi
import com.prodmobile.template.data.accounts.student.remote.MentorReviewDto
import com.prodmobile.template.data.accounts.student.remote.StudentResponseDto
import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import com.prodmobile.template.feature.favourite.view_model.FavouriteElement
import com.prodmobile.template.feature.requests.model.RequestStatus
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
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

class MentorsAccountsRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val templateApi: TemplateApi
) : MentorAccountRemoteDataSource {
    override suspend fun getCurrentAccountInfo(jwtToken: String): ResponseResult<MentorInfoDto, NetworkError> {
        return safeCall<MentorInfoDto> {
            val url = templateApi.url
                .appendPathSegments("mentor", "me")
                .build()
            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun getStudentAccountById(
        mentorId: String,
        jwtToken: String
    ): ResponseResult<StudentResponseDto, NetworkError> {
        return safeCall<StudentResponseDto> {
            val url = templateApi.url
                .appendPathSegments("student", mentorId)
                .build()

            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override suspend fun observeMatchRequests(jwtToken: String): Flow<ResponseResult<List<MatchRequestFromMentorDto>, NetworkError>> {
        return flow {
            while (true) {
                runCatching {
                    val result = safeCall<List<MatchRequestFromMentorDto>> {
                        val url = templateApi.url
                            .appendPathSegments("mentor", "request")
                            .build()
                        httpClient.get(url) {
                            bearerAuth(jwtToken)
                        }
                    }
                    emit(result)
                    delay(1000)
                }.onFailure {
                    println("NPE ERROR PROB")
                    it.printStackTrace()
                }
            }
        }
    }

    override suspend fun getOwnReviews(
        jwtToken: String
    ): ResponseResult<List<MentorReviewDto>, NetworkError> {
        return safeCall<List<MentorReviewDto>> {
            val url = templateApi.url
                .appendPathSegments("review", "")
                .build()
            httpClient.get(url) {
                bearerAuth(jwtToken)
            }
        }
    }

    override fun getFavorites(jwtToken: String): List<FavouriteElement> {
        return listOf(
            FavouriteElement(
                id = "1",
                name = "Favourite Mentor 1",
                description = "Description of Favourite Mentor 1",
                imageUrl = "https://example.com/mentor1.jpg",
                skills = listOf(),
                contacts = listOf()
            ),
            FavouriteElement(
                id = "2",
                name = "Favourite Mentor 2",
                description = "Description of Favourite Mentor 2",
                imageUrl = "https://example.com/mentor2.jpg",
                skills = listOf(),
                contacts = listOf()
            ),
            FavouriteElement(
                id = "3",
                name = "Favourite Mentor 3",
                description = "Description of Favourite Mentor 3",
                imageUrl = "https://example.com/mentor3.jpg",
                skills = listOf(),
                contacts = listOf()
            )
        )
    }

    override suspend fun updateInfo(data: UpdatedMentorInfo, token: String) {
        val url = templateApi.url
            .appendPathSegments("mentor")
            .build()
        httpClient.put(url) {
            bearerAuth(token)
            setBody(data)
        }
    }

    override suspend fun declineMatchRequest(requestId: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("mentor", "request", "verdict")
                .build()
            httpClient.post(url) {
                bearerAuth(jwtToken)
                setBody(
                    MentorVerdictDto(
                        requestId,
                        RequestStatus.Rejected
                    )
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    override suspend fun acceptMatchRequest(requestId: String, jwtToken: String) {
        runCatching {
            val url = templateApi.url
                .appendPathSegments("mentor", "request", "verdict")
                .build()
            httpClient.post(url) {
                bearerAuth(jwtToken)
                setBody(
                    MentorVerdictDto(
                        requestId,
                        RequestStatus.Accepted
                    )
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }
}

@Serializable
data class UpdatedMentorInfo(
    val age: Long,
    val description: String,
    val contacts: List<MentorContact>,
    val skills: List<String>
)

@Serializable
data class MatchRequestFromMentorDto(
    val id: String,
    val type: RequestStatus,
    @SerialName("created_at")
    val createdAt: String,
    val student: StudentResponseDto
)