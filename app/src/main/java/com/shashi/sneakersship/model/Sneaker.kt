package com.shashi.sneakersship.model

import java.io.Serializable

data class Sneaker(
    val id: String,
    val brand: String,
    val colorway: String,
    val gender: String,
    val media: Media,
    val releaseDate: String,
    val retailPrice: Double,
    val styleId: String,
    val shoe: String,
    val name: String,
    val title: String,
    val year: Int
) : Serializable