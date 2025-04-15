package com.prodmobile.template.data.auth.mentor.remote

import com.prodmobile.template.core.http_client.safeCall
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.core.result.onSuccess
import com.prodmobile.template.data.TemplateApi
import com.prodmobile.template.data.auth.mentor.MentorAuthRemoteDataSource
import com.prodmobile.template.data.auth.mentor.model.MentorContactDto
import com.prodmobile.template.data.auth.mentor.model.MentorSignInData
import com.prodmobile.template.data.auth.mentor.model.MentorSignInRequestDto
import com.prodmobile.template.data.auth.mentor.model.MentorSignUpData
import com.prodmobile.template.data.auth.mentor.model.MentorSignUpRequestDto
import com.prodmobile.template.data.auth.model.SignResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.appendPathSegments

class MentorAuthRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val templateApi: TemplateApi
) : MentorAuthRemoteDataSource {
    override suspend fun signIn(signInData: MentorSignInData): ResponseResult<SignResponseDto, NetworkError> {
        return safeCall<SignResponseDto> {
            val authRequest = MentorSignInRequestDto(
                signInData.login,
            )

            httpClient.post(
                templateApi.url.appendPathSegments("mentor", "sign_in").build()
            ) {
                setBody(authRequest)
            }
        }
    }

    override suspend fun attachImage(image: ByteArray, jwtToken: String) {
        runCatching {
            httpClient.put(
                templateApi.url.appendPathSegments("mentor", "attach").build()
            ) {
                bearerAuth(jwtToken)
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = image,
                                headers = Headers.build {
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"file\"; filename=\"avatar.png\""
                                    )
                                }
                            )
                        }
                    )
                )
            }
        }
    }

    override suspend fun signUp(signUpData: MentorSignUpData): ResponseResult<SignResponseDto, NetworkError> {
        return safeCall<SignResponseDto> {
            val authRequest = MentorSignUpRequestDto(
                name = signUpData.fullName,
                description = signUpData.description,
                contacts = signUpData
                    .contacts
                    .map {
                        MentorContactDto(
                            messenger = it.messenger,
                            id = it.id,
                        )
                    },
                skills = signUpData.skills
            )

            httpClient.post(
                templateApi.url.appendPathSegments("mentor", "sign_up").build()
            ) {
                setBody(authRequest)
            }
        }.onSuccess {
            if (signUpData.bytes != null) {
                attachImage(signUpData.bytes, it.accessToken)
            }
        }
    }
}
