package com.prodmobile.template.data.auth.mentor.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MentorSignInData(
    val login: String
)

@Serializable
data class MentorSignInRequestDto(
    @SerialName("full_name")
    val name: String
)