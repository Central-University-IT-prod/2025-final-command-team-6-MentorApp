package com.prodmobile.template.data.auth.student.remote

import com.prodmobile.template.core.http_client.safeCall
import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.core.result.onSuccess
import com.prodmobile.template.data.TemplateApi
import com.prodmobile.template.data.auth.model.SignResponseDto
import com.prodmobile.template.data.auth.student.StudentAuthRemoteDataSource
import com.prodmobile.template.data.auth.student.model.StudentSignInData
import com.prodmobile.template.data.auth.student.model.StudentSignInRequestDto
import com.prodmobile.template.data.auth.student.model.StudentSignUpData
import com.prodmobile.template.data.auth.student.model.StudentSignUpRequestDto
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

class StudentAuthRemoteDataSourceImpl(
    private val httpClient: HttpClient,
    private val templateApi: TemplateApi
) : StudentAuthRemoteDataSource {
    override suspend fun signIn(signInData: StudentSignInData): ResponseResult<SignResponseDto, NetworkError> {
        return safeCall<SignResponseDto> {
            val authRequest = StudentSignInRequestDto(
                signInData.login,
            )

            httpClient.post(
                templateApi.url.appendPathSegments("student", "sign_in").build()
            ) {
                setBody(authRequest)
            }
        }
    }

    override suspend fun signUp(signUpData: StudentSignUpData): ResponseResult<SignResponseDto, NetworkError> {
        return safeCall<SignResponseDto> {
            val authRequest = StudentSignUpRequestDto(
                signUpData.fullName,
                signUpData.age,
                signUpData.interests
            )

            httpClient.post(
                templateApi.url.appendPathSegments("student", "sign_up").build()
            ) {
                setBody(authRequest)
            }
        }.onSuccess {
            if (signUpData.bytes != null) {
                attachImage(signUpData.bytes, it.accessToken)
            }
        }
    }

    override suspend fun attachImage(image: ByteArray, jwtToken: String) {
        runCatching {
            httpClient.put(
                templateApi.url.appendPathSegments("student", "attach").build()
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
        }.onFailure {
            it.printStackTrace()
        }
    }
}
