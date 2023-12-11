package com.shashi.sneakersship.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.shashi.sneakersship.model.CartItem
import com.shashi.sneakersship.model.Sneaker
import com.shashi.sneakersship.repository.CartRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    private val _cartItems: MutableLiveData<List<CartItem>> = MutableLiveData(emptyList())
    private val _totalPrice: MutableLiveData<Double> = MutableLiveData(0.0)
    private val _totalItems: MutableLiveData<Int> = MutableLiveData(0)

    val cartItems: LiveData<List<CartItem>>
        get() = _cartItems
    val totalPrice: LiveData<Double>
        get() = _totalPrice
    val totalItems: LiveData<Int>
        get() = _totalItems

    init {
        getCartItems()
    }

    private fun getCartItems() = viewModelScope.launch {
        repository.getCartItems().collect { cartItemList ->
            _cartItems.value = cartItemList
            _totalItems.value = cartItemList.sumOf { it.quantity }
            _totalPrice.value = cartItemList.sumOf { it.sneaker.retailPrice * it.quantity }
        }
    }

    fun addToCart(item: Sneaker, size: Int, count: Int = 1) = viewModelScope.launch {
        val cartItem = _cartItems.asFlow().map { cartItemList ->
            cartItemList.firstOrNull { it.sneaker.id == item.id }
        }.firstOrNull()

        if (cartItem == null) {
            repository.addItemToCart(CartItem(sneaker = item, size = size, quantity = count))
        } else {
            repository.updateItemCountInCart(cartItem.id!!, cartItem.quantity + count)
        }
    }

    fun removeFromCart(item: Sneaker, count: Int = 1) = viewModelScope.launch {
        val cartItem = _cartItems.asFlow().map { cartItemList ->
            cartItemList.firstOrNull { it.sneaker.id == item.id }
        }.firstOrNull()

        if (cartItem != null) {
            if (cartItem.quantity == count || cartItem.quantity == 1) {
                repository.removeItemFromCart(cartItem)
            } else {
                repository.updateItemCountInCart(cartItem.id!!, cartItem.quantity - 1)
            }
        }
    }
}