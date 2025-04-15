package com.prodmobile.template.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import coil3.request.ImageRequest
import com.prodmobile.template.R

@Composable
fun NetworkImage(
    image: ByteArray?,
    modifier: Modifier = Modifier,
    forceLoading: Boolean = false,
    contentDescription: String? = null,
    placeholder: @Composable () -> Unit = { ImagePlaceholder(modifier) }
) {
    NetworkImage(
        model = image,
        modifier = modifier,
        forceLoading = forceLoading,
        contentDescription = contentDescription,
        placeholder = placeholder
    )
}

@Composable
fun NetworkImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    forceLoading: Boolean = false,
    contentDescription: String? = null,
    placeholder: @Composable () -> Unit = { ImagePlaceholder(modifier) }
) {
    NetworkImage(
        model = if (imageUrl.isNullOrBlank()) {
            null
        } else {
            ImageRequest.Builder(LocalPlatformContext.current)
                .data(imageUrl)
                .build()
        },
        modifier = modifier,
        forceLoading = forceLoading,
        contentDescription = contentDescription,
        placeholder = placeholder
    )
}

@Composable
fun NetworkImage(
    model: Any?,
    modifier: Modifier = Modifier,
    forceLoading: Boolean = false,
    contentDescription: String? = null,
    placeholder: @Composable () -> Unit = { ImagePlaceholder(modifier) }
) {
    if (model == null) {
        placeholder()
    } else {
        SubcomposeAsyncImage(
            modifier = modifier.fillMaxSize(),
            model = model,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop
        ) {
            val state by painter.state.collectAsState()

            if (forceLoading) {
                ShimmerEffect(
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                when (state) {
                    is AsyncImagePainter.State.Success -> {
                        SubcomposeAsyncImageContent(
                            contentScale = ContentScale.Crop
                        )
                    }

                    is AsyncImagePainter.State.Loading -> {
                        ShimmerEffect(
                            modifier = Modifier
                                .fillMaxSize()
                        )
                    }

                    is AsyncImagePainter.State.Error -> {
                        placeholder()
                    }

                    else -> Unit
                }
            }

        }
    }
}

@Composable
private fun ImagePlaceholder(modifier: Modifier) {
    Image(
        painterResource(R.drawable.no_image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxSize()
    )
}
