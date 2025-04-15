package com.prodmobile.template.feature.account.view_model.mentor

sealed interface MentorInfoEvent {
    data object SendMatchRequest : MentorInfoEvent
    data class SendReview(val reviewText: String) : MentorInfoEvent
}
