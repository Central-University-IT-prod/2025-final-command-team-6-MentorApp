package com.prodmobile.template.feature.account.view_model.mentor

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class MentorInfoState(
    val id: String,
    val fullName: String = "Василий Опытный",
    val avatarUrl: String? = "https://upload.wikimedia.org/wikipedia/commons/3/3a/Cat03.jpg",
    val mentorDescription: String? = "I am a mentor, probably",
    val contacts: List<MentorContact> = listOf(
        MentorContact("tg", "@tgPobeda"),
        MentorContact("gmail", "google@gmail.com"),
    ),
    val isFavorite: Boolean = false,
    val skills: List<String> = listOf(),
    val reviews: List<ReviewInfo> = listOf(),
    val canMakeComments: Boolean = false
)

@Serializable
data class MentorContact(
    @SerialName("social_network")
    val messenger: String,
    @SerialName("url")
    val id: String,
)

@Serializable
data class ReviewInfo(
    val username: String,
    val imageUrl: String?,
    val description: String
)