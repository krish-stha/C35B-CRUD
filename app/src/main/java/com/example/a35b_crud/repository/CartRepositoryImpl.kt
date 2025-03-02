package com.example.a35b_crud.repository

import com.example.a35b_crud.model.CartModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartRepositoryImpl : CartRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val reference: DatabaseReference = database.reference.child("cart")

    override fun addCartItem(cartModel: CartModel, callback: (Boolean, String) -> Unit) {
        val id = reference.push().key.toString()
        cartModel.cartId = id
        reference.child(id).setValue(cartModel)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Item added to cart successfully")
                } else {
                    callback(false, it.exception?.message ?: "Error adding item")
                }
            }
    }

    override fun deleteCartItem(cartId: String, callback: (Boolean, String) -> Unit) {
        reference.child(cartId).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    callback(true, "Item removed from cart successfully")
                } else {
                    callback(false, it.exception?.message ?: "Error deleting item")
                }
            }
    }

    override fun getAllCartItems(callback: (List<CartModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val cartItems = mutableListOf<CartModel>()
                if (snapshot.exists()){
                    for (eachItem in snapshot.children) {
                        val cartModel = eachItem.getValue(CartModel::class.java)
                        if(cartModel != null){
                            cartItems.add(cartModel)
                        }
                    }
                    callback(cartItems, true, "Cart items fetched successfully")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null, false, error.message)
            }
        })
    }

    override fun updateCartItem(cartId: String, data: MutableMap<String, Any>, callback: (Boolean, String) -> Unit) {
        reference.child(cartId).updateChildren(data)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    callback(true, "Cart item updated successfully")
                } else {
                    callback(false, it.exception?.message ?: "Error updating cart item")
                }
            }
    }
}
