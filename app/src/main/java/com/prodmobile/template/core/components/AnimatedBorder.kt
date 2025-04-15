package com.prodmobile.template.core.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedBorder(
    modifier: Modifier = Modifier,
    isActive: Boolean,
    colors: List<Color> = listOf(Color(0xFFF44336), Color(0xFFF50057), Color(0xFF690909)),
    shape: Shape = MaterialTheme.shapes.medium,
    durationMillis: Int = 2000,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "border_animation")

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradient_progress"
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isActive) 4.dp else 0.dp,
        animationSpec = tween(700),
        label = "border_width"
    )

    Box(
        modifier = modifier
            .drawWithContent {
                drawContent()
                if (isActive) {
                    val brush = Brush.linearGradient(
                        colors = colors,
                        start = Offset(
                            x = -size.width * (1 - progress),
                            y = -size.height * (1 - progress)
                        ),
                        end = Offset(
                            x = size.width * progress,
                            y = size.height * progress
                        )
                    )

                    val outline = shape.createOutline(size, layoutDirection, this)
                    drawOutline(
                        outline = outline,
                        brush = brush,
                        style = Stroke(width = borderWidth.toPx())
                    )
                }
            }
    ) {
        content()
    }
}