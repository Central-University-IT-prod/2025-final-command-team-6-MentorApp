package com.prodmobile.template.feature.requests.model

import com.prodmobile.template.feature.account.view_model.mentor.MentorContact
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class RequestFromMentorModel(
    val requestId: String,
    val studentId: String,
    val studentName: String,
    val interests: List<String>,
    val createdAt: String,
    val imageUrl: String?,
    val status: RequestStatus
)


data class RequestFromStudentModel(
    val requestId: String,
    val mentorId: String,
    val name: String,
    val imageUrl: String?,
    val createdAt: String,
    val contacts: List<MentorContact>,
    val skills: List<String>,
    val status: RequestStatus
)

@Serializable
enum class RequestStatus(val title: String) {
    @SerialName("review")
    Review("На рассмотрении"),

    @SerialName("rejected")
    Rejected("Отклонено"),

    @SerialName("accepted")
    Accepted("Принято")
}