package com.prodmobile.template.core.models

import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import com.prodmobile.template.feature.account.view_model.mentor.ReviewInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MentorInfoDto(
    val id: String,
    @SerialName("full_name")
    val fullName: String = "Василий Опытный",
    @SerialName("photo_url")
    val avatarUrl: String? = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg",
    val description: String? = "Your description",
    @SerialName("contacts")
    val contacts: List<MentorContact> = listOf(
        MentorContact("tg", "@tgPobeda"),
        MentorContact("gmail", "google@gmail.com"),
    ),
    @SerialName("skills")
    val skills: List<String> = listOf("Математика")
)