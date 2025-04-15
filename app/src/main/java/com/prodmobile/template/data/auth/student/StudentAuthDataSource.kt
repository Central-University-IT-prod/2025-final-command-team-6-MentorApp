package com.prodmobile.template.data.auth.student

import com.prodmobile.template.core.result.NetworkError
import com.prodmobile.template.core.result.ResponseResult
import com.prodmobile.template.data.auth.model.SignResponseDto
import com.prodmobile.template.data.auth.student.model.StudentSignInData
import com.prodmobile.template.data.auth.student.model.StudentSignUpData

interface StudentAuthRemoteDataSource {
    suspend fun signIn(signInData: StudentSignInData): ResponseResult<SignResponseDto, NetworkError>
    suspend fun signUp(signUpData: StudentSignUpData): ResponseResult<SignResponseDto, NetworkError>
    suspend fun attachImage(image: ByteArray, jwtToken: String)
}
