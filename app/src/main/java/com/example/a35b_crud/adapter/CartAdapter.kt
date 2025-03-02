package com.example.a35b_crud.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a35b_crud.R
import com.example.a35b_crud.model.CartModel
import com.squareup.picasso.Picasso

class CartAdapter(
    private var cartList: List<CartModel>,
    private val onDeleteClick: (CartModel) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartItem = cartList[position]
        holder.productName.text = cartItem.productName
        holder.productPrice.text = "$${cartItem.price}"
        holder.productQuantity.text = "Qty: ${cartItem.quantity}"
        Picasso.get().load(cartItem.imageUrl).into(holder.productImage)
        holder.btnDelete.setOnClickListener { onDeleteClick(cartItem) }
    }

    override fun getItemCount(): Int = cartList.size

    fun setCartItems(cartItems: List<CartModel>) {
        cartList = cartItems
        notifyDataSetChanged()
    }
}
