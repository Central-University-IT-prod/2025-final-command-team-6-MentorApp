package com.prodmobile.template.core.models

import kotlinx.serialization.Serializable


/**
 * we store user role only when user finished registration, so we know for sure that then he is on the home screen
 */
@Serializable
data class AccountInfo(
    val userId: String,
    val userRole: UserRole,
    val jwtToken: String,
)