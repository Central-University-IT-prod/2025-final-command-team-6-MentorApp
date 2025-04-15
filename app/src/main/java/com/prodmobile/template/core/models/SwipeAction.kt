package com.prodmobile.template.core.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SwipeAction {
    @SerialName("like")
    LIKE,

    @SerialName("dislike")
    DISLIKE,

    @SerialName("favorites")
    FAVORITES
}