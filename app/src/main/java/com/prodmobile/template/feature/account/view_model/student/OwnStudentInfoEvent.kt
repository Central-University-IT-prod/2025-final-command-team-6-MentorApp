package com.prodmobile.template.feature.account.view_model.student

sealed interface OwnStudentInfoEvent {
    data object SignOut : OwnStudentInfoEvent
}