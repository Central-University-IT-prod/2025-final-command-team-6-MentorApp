package com.prodmobile.template.feature.feed.view_model

sealed interface FeedScreenEvent {
    data object Like: FeedScreenEvent
    data object Dislike: FeedScreenEvent
    data object Favorite: FeedScreenEvent
    data object RemoveFromFavourite: FeedScreenEvent
}