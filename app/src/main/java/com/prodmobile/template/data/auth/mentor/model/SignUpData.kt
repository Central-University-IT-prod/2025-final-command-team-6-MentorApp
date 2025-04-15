package com.prodmobile.template.data.auth.mentor.model

import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MentorSignUpData(
    @SerialName("full_name")
    val fullName: String,
    val description: String?,
    val contacts: List<MentorContact>,
    val skills: List<String>,
    val bytes: ByteArray?
)

@Serializable
data class MentorContactDto(
    @SerialName("social_network")
    val messenger: String,
    @SerialName("url")
    val id: String,
)

@Serializable
data class MentorSignUpRequestDto(
    @SerialName("full_name")
    val name: String,
    val description: String?,
    val contacts: List<MentorContactDto>,
    val skills: List<String>,
)