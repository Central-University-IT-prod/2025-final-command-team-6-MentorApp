package com.prodmobile.template.feature.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.prodmobile.template.core.components.NetworkImage
import com.prodmobile.template.core.components.ShimmerEffect
import com.prodmobile.template.feature.feed.components.SwipeCard
import com.prodmobile.template.feature.feed.view_model.FeedScreenEvent
import com.prodmobile.template.feature.feed.view_model.FeedScreenState

@Composable
fun FeedScreen(
    state: FeedScreenState,
    onEvent: (FeedScreenEvent) -> Unit,
    navigateToMentorScreen: (String) -> Unit,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    if (!state.isLoading && state.feed.isNotEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
                .blur(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxHeight(0.5f)
                    .width(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 32.dp,
                            bottomEnd = 32.dp,
                        )
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Red.copy(0.7f),
                                Color.Red.copy(0f)
                            ),
                            radius = 500f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight(0.5f)
                    .width(4.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 32.dp,
                            bottomStart = 32.dp,
                        )
                    )
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Green.copy(0.7f),
                                Color.Green.copy(0f)
                            ),
                            radius = 500f
                        )
                    )
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.feed.size == state.currentIndex) {
            if (state.isLoading) {
                ShimmerEffect(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.7f)
                )
            } else {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.7f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "У нас больше нет менторов, которых мы могли бы посоветовать",
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        Box {
            state.feed.forEachIndexed { index, element ->
                if (element != null) {
                    SwipeCard(
                        isTopCard = index == state.currentIndex,
                        100 - index.toFloat(),
                        swipeThreshold = 50f,
                        onSwipeLeft = { onEvent(FeedScreenEvent.Dislike) },
                        onSwipeRight = { onEvent(FeedScreenEvent.Like) }
                    ) {
                        Column(
                            Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(MaterialTheme.colorScheme.surfaceContainer)
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.7f)
                                .zIndex((index).toFloat())
                                .clickable {
                                    navigateToMentorScreen(element.id)
                                }
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            NetworkImage(
                                imageUrl = element.avatarUrl,
                                modifier = Modifier
                                    .clip(MaterialTheme.shapes.medium)
                                    .weight(0.2f)
                            )

                            Text(
                                text = element.fullName,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(8.dp)
                            )

                            if (element.mentorDescription != null)
                                Text(
                                    text = element.mentorDescription,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Center,
                                )

                            Spacer(Modifier.height(8.dp))

                            val currentFavorite = element.isFavorite
                            FilledTonalIconButton({
                                if (currentFavorite)
                                    onEvent(FeedScreenEvent.RemoveFromFavourite)
                                else
                                    onEvent(FeedScreenEvent.Favorite)
                            }) {
                                Icon(
                                    if (currentFavorite) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                                    ""
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
    }
}