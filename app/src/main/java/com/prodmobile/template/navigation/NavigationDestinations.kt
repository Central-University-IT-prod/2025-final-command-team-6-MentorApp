package com.prodmobile.template.navigation

import com.prodmobile.template.core.models.AccountInfo
import kotlinx.serialization.Serializable

@Serializable
sealed interface NavigationPath {
    /**
     * this is for the first screen, once user chooses his role we switch to appropriate path
     */
    @Serializable
    data object WelcomePath : NavigationPath

    @Serializable
    data object AdminPath : NavigationPath

    @Serializable
    data class StudentPath(val accountInfo: AccountInfo) : NavigationPath

    @Serializable
    data class MentorPath(val accountInfo: AccountInfo) : NavigationPath
}
