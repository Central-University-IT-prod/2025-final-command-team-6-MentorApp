package com.prodmobile.template.feature.favourite.view_model

sealed interface FavouritesEvents {
    data class DeleteFromFavourite(val index: Int, val mentorId: String): FavouritesEvents
}