package com.example.a35b_crud.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a35b_crud.R
import com.example.a35b_crud.model.ProductModel
import com.squareup.picasso.Picasso

class ProductAdapter(
    private var productList: List<ProductModel>,
    private val onEditClick: (ProductModel) -> Unit,
    private val onDeleteClick: (ProductModel) -> Unit,
    private val onAddToCartClick: (ProductModel) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        val btnAddToCart: Button = itemView.findViewById(R.id.btnAddToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.text = product.productName
        holder.productPrice.text = "$${product.price}"
        Picasso.get().load(product.imageUrl).into(holder.productImage)
        holder.btnEdit.setOnClickListener { onEditClick(product) }
        holder.btnDelete.setOnClickListener { onDeleteClick(product) }
        holder.btnAddToCart.setOnClickListener { onAddToCartClick(product) }
    }

    override fun getItemCount(): Int = productList.size

    fun setProducts(products: List<ProductModel>) {
        productList = products
        notifyDataSetChanged()
    }
}
