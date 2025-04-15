package com.prodmobile.template.core.models

import com.prodmobile.template.feature.requests.model.RequestStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MentorVerdictDto(
    @SerialName("mentoring_request_id")
    val requestId: String,
    val type: RequestStatus,
)