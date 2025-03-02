package com.example.a35b_crud.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a35b_crud.databinding.FragmentHomeBinding
import com.example.a35b_crud.model.CartModel
import com.example.a35b_crud.model.ProductModel
import com.example.a35b_crud.repository.CartRepositoryImpl
import com.example.a35b_crud.repository.ProductRepistoryImpl
import com.example.a35b_crud.ui.activity.AddProductActivity
import com.example.a35b_crud.ui.adapter.ProductAdapter
import com.example.a35b_crud.viewmodel.CartViewModel
import com.example.a35b_crud.viewmodel.ProductViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    lateinit var productViewModel: ProductViewModel
    lateinit var cartViewModel: CartViewModel
    lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize product and cart view models
        productViewModel = ProductViewModel(ProductRepistoryImpl())
        cartViewModel = CartViewModel(CartRepositoryImpl())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        productAdapter = ProductAdapter(
            listOf(),
            onEditClick = { product ->
                // Launch AddProductActivity in edit mode
                val intent = Intent(requireContext(), AddProductActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },
            onDeleteClick = { product ->
                productViewModel.deleteProduct(product.productId) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    if (success) productViewModel.getAllProduct()
                }
            },
            onAddToCartClick = { product ->
                // Create a CartModel from ProductModel and add to cart
                val cartItem = CartModel(
                    cartId = "",
                    productId = product.productId,
                    productName = product.productName,
                    productDesc = product.productDesc,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    quantity = 1
                )
                cartViewModel.addCartItem(cartItem) { success, message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                }
            }
        )
        binding.recyclerView.adapter = productAdapter

        binding.fabAddProduct.setOnClickListener {
            startActivity(Intent(requireContext(), AddProductActivity::class.java))
        }

        // Observe product list
        productViewModel.getAllProduct()
        productViewModel._allProducts.observe(viewLifecycleOwner, Observer { products ->
            products?.let { productAdapter.setProducts(it) }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
