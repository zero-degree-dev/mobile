package com.example.zero_degree.ui.image

import android.widget.ImageView
import coil.ImageLoader
import coil.imageLoader
import coil.request.Disposable
import coil.request.ImageRequest

class ImageLoader {
    inline fun ImageView.load(
        data: Any?,
        imageLoader: ImageLoader = context.imageLoader,
        builder: ImageRequest.Builder.() -> Unit = {}
    ): Disposable {
        val request = ImageRequest.Builder(context)
            .data(data)
            .target(this)
            .apply(builder)
            .build()
        return imageLoader.enqueue(request)
    }
}