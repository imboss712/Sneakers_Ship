package com.shashi.sneakersship.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shashi.sneakersship.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface SneakerDao {

    @Query("SELECT * FROM sneakers")
    fun getCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToCart(cartItem: CartItem)

    @Delete
    suspend fun removeFromCart(cartItem: CartItem)

    @Query("UPDATE sneakers SET quantity=:updatedItemCount WHERE id=:cartItemId")
    suspend fun updateItemCount(cartItemId: Int, updatedItemCount: Int)

}