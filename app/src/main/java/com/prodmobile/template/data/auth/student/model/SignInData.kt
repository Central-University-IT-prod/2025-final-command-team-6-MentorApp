package com.prodmobile.template.data.auth.student.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentSignInData(
    val login: String
)

@Serializable
data class StudentSignInRequestDto(
    @SerialName("full_name")
    val name: String
)