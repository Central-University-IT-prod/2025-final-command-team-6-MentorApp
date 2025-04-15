package com.prodmobile.template.core.models

import kotlinx.serialization.Serializable

@Serializable
sealed interface UserRole {
    @Serializable
    data object Student : UserRole

    @Serializable
    data object Mentor : UserRole

    @Serializable
    data object Unauthorized : UserRole
}