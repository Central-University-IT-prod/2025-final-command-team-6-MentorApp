package com.prodmobile.template.data.auth.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignResponseDto(
    val id: String,
    @SerialName("access_token")
    val accessToken: String,
)