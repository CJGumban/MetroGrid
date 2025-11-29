package com.example.metrogrid.ui.composable

import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.compose.MetroGridTheme
import com.example.metrogrid.R


@Composable
fun LoadingScreen() {
    Box(
        Modifier.fillMaxSize()

    ) {

        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                add(GifDecoder.Factory())
            }
            .build()

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.train_red) // your local GIF
                .size(150)
                .build(),
            imageLoader = imageLoader
        )

        Image(
            painter = painter,
            contentDescription = "Animated GIF",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
                .graphicsLayer {

                    clip = true
                    shape = CircleShape
                }
        )




    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoadingScreenPreview() {
    MetroGridTheme {
        LoadingScreen()
    }
}