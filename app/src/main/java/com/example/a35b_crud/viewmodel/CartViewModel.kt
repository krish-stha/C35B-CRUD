package com.example.a35b_crud.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.a35b_crud.model.CartModel
import com.example.a35b_crud.repository.CartRepository

class CartViewModel(val repository: CartRepository) {

    private val _allCartItems = MutableLiveData<List<CartModel>?>()
    val allCartItems: LiveData<List<CartModel>?> get() = _allCartItems

    fun addCartItem(cartModel: CartModel, callback: (Boolean, String) -> Unit) {
        repository.addCartItem(cartModel, callback)
    }

    fun getAllCartItems() {
        repository.getAllCartItems { cartItems, success, message ->
            if (success) {
                _allCartItems.value = cartItems
            }
        }
    }

    fun deleteCartItem(cartId: String, callback: (Boolean, String) -> Unit) {
        repository.deleteCartItem(cartId, callback)
    }

    fun updateCartItem(cartId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        repository.updateCartItem(cartId, data, callback)
    }
}
