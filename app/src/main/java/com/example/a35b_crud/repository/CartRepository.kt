package com.example.a35b_crud.repository

import com.example.a35b_crud.model.CartModel

interface CartRepository {
    fun addCartItem(cartModel: CartModel, callback: (Boolean, String) -> Unit)
    fun deleteCartItem(cartId: String, callback: (Boolean, String) -> Unit)
    fun getAllCartItems(callback: (List<CartModel>?, Boolean, String) -> Unit)
    fun updateCartItem(cartId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit)
}
