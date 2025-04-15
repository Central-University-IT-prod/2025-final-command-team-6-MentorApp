package com.prodmobile.template.feature.feed.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.zIndex
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeCard(
    isTopCard: Boolean = false,
    zIndex: Float,
    onSwipeLeft: () -> Unit = {},
    onSwipeRight: () -> Unit = {},
    swipeThreshold: Float = 150f,
    maxRotationAngle: Float = 15f,
    cardScale: Float = 0.9f,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current.density
    val offsetX = remember { Animatable(0f) }
    val rotationYs = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        offsetX.snapTo(0f)
        rotationYs.snapTo(0f)
    }

    Box(
        modifier = Modifier
            .zIndex(zIndex)
            .graphicsLayer {
                translationX = offsetX.value
                rotationY = rotationYs.value
                scaleX = 1f - abs(offsetX.value) * (1 - cardScale) / 1000f
                scaleY = 1f - abs(offsetX.value) * (1 - cardScale) / 1000f
                cameraDistance = 12 * density
            }
            .then(
                if (isTopCard) {
                    Modifier
                        .pointerInput(Unit) {
                            detectHorizontalDragGestures(
                                onHorizontalDrag = { change, dragAmount ->
                                    scope.launch {
                                        offsetX.snapTo(offsetX.value + dragAmount / density)
                                        rotationYs.snapTo(
                                            (offsetX.value / swipeThreshold) * maxRotationAngle
                                        )
                                    }
                                    change.consume()
                                },
                                onDragEnd = {
                                    scope.launch {
                                        when {
                                            offsetX.value > swipeThreshold -> {
                                                offsetX.animateTo(1000f, animationSpec = tween(300))
                                                onSwipeRight()
                                            }

                                            offsetX.value < -swipeThreshold -> {
                                                offsetX.animateTo(
                                                    -1000f,
                                                    animationSpec = tween(300)
                                                )
                                                onSwipeLeft()
                                            }

                                            else -> {
                                                offsetX.animateTo(0f, tween(300))
                                                rotationYs.animateTo(0f, tween(300))
                                            }
                                        }
                                    }
                                }
                            )
                        }
                } else {
                    Modifier
                }
            )

    ) {
        content()
    }
}