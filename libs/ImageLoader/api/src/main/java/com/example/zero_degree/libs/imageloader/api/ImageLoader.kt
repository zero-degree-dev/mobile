package com.example.zero_degree.libs.imageloader.api

interface ImageLoader<T> {
    fun loadImage(imageView: T, model: Any?)
}

