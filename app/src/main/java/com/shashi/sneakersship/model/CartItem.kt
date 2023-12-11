package com.shashi.sneakersship.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sneakers"
)
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val sneaker: Sneaker,
    val size: Int,
    val quantity: Int
)