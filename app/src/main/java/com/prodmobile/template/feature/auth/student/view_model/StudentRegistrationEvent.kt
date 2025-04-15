package com.prodmobile.template.feature.auth.student.view_model

import android.content.ContentResolver
import android.net.Uri

sealed interface StudentRegistrationEvent {
    data class NameUpdate(val name: String) : StudentRegistrationEvent
    data class WantedSkillsUpdate(val skills: List<String>) : StudentRegistrationEvent
    data class AvatarUpdate(val imageUri: Uri, val contentResolver: ContentResolver) : StudentRegistrationEvent
    data object SignUp : StudentRegistrationEvent
    data object SignIn : StudentRegistrationEvent
}