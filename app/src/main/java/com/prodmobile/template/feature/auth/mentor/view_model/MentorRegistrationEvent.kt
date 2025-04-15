package com.prodmobile.template.feature.auth.mentor.view_model

import android.content.ContentResolver
import android.net.Uri
import com.prodmobile.template.feature.account.view_model.mentor.MentorContact

sealed interface MentorRegistrationEvent {
    data class NameUpdate(val name: String) : MentorRegistrationEvent
    data class SkillsUpdate(val skills: List<String>) : MentorRegistrationEvent
    data class AboutUpdate(val about: String?) : MentorRegistrationEvent
    data class ContactsUpdate(val contacts: List<MentorContact>) : MentorRegistrationEvent
    data class AvatarUpdate(val imageUri: Uri, val contentResolver: ContentResolver) : MentorRegistrationEvent
    data object SignUp : MentorRegistrationEvent
    data object SignIn : MentorRegistrationEvent
}