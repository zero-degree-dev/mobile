package com.example.zero_degree.libs.imageloader.impl

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil3.load
import com.example.zero_degree.libs.imageloader.api.ImageLoader

class CoilImageLoader : ImageLoader<ImageView> {
    
    override fun loadImage(imageView: ImageView, model: Any?) {
        when (model) {
            is String -> {
                imageView.load(model)
            }
            is Int -> {
                imageView.setImageResource(model)
            }
            null -> {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
            else -> {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }
    }
}

