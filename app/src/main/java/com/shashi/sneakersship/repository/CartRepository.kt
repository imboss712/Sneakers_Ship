package com.shashi.sneakersship.repository

import com.shashi.sneakersship.db.SneakerDao
import com.shashi.sneakersship.model.CartItem

class CartRepository(private val dao: SneakerDao) {

    fun getCartItems() = dao.getCartItems()

    suspend fun addItemToCart(cartItem: CartItem) = dao.addToCart(cartItem)

    suspend fun removeItemFromCart(cartItem: CartItem) = dao.removeFromCart(cartItem)

    suspend fun updateItemCountInCart(cartItemId: Int, updatedItemCount: Int) =
        dao.updateItemCount(cartItemId, updatedItemCount)
}