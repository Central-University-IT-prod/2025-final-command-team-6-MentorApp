package com.prodmobile.template.feature.account.view_model.mentor

sealed interface OwnMentorInfoEvent {
    data object SignOut : OwnMentorInfoEvent
}
