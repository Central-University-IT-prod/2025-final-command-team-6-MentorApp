package com.prodmobile.template.data.auth.mentor

import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.auth.mentor.model.MentorSignInData
import com.prodmobile.template.data.auth.mentor.model.MentorSignUpData
import com.prodmobile.template.data.auth.model.SignResponseDto

interface MentorAuthRemoteDataSource {
    suspend fun signIn(signInData: MentorSignInData): ResponseResult<SignResponseDto, NetworkError>
    suspend fun signUp(signUpData: MentorSignUpData): ResponseResult<SignResponseDto, NetworkError>
    suspend fun attachImage(image: ByteArray, jwtToken: String)
}