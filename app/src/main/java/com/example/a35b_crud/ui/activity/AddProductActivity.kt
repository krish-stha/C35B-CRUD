package com.example.a35b_crud.ui.activity

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.a35b_crud.R
import com.example.a35b_crud.databinding.ActivityAddProductBinding
import com.example.a35b_crud.model.ProductModel
import com.example.a35b_crud.repository.ProductRepistoryImpl
import com.example.a35b_crud.utils.ImageUtils
import com.example.a35b_crud.utils.LoadingUtils
import com.example.a35b_crud.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class AddProductActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddProductBinding
    lateinit var productViewModel: ProductViewModel
    lateinit var loadingUtils: LoadingUtils
    lateinit var imageUtils: ImageUtils

    var imageUri: Uri? = null
    var isEditMode = false
    var editingProduct: ProductModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        imageUtils = ImageUtils(this)
        loadingUtils = LoadingUtils(this)
        productViewModel = ProductViewModel(ProductRepistoryImpl())

        // Check if launched in edit mode
        if (intent.hasExtra("product")) {
            isEditMode = true
            editingProduct = intent.getParcelableExtra("product")
            populateProductData()
        }

        imageUtils.registerActivity { url ->
            url?.let {
                imageUri = it
                Picasso.get().load(it).into(binding.imageBrowse)
            }
        }

        binding.imageBrowse.setOnClickListener {
            imageUtils.launchGallery(this)
        }

        binding.btnAddProduct.setOnClickListener {
            // If an image was selected, upload it; if not in add mode then in edit mode keep existing image.
            if (imageUri != null) {
                uploadImage()
            } else {
                if (isEditMode) {
                    // No new image selected; update product with current image
                    updateProduct(null)
                } else {
                    Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Adjust UI for system insets if needed
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun populateProductData() {
        editingProduct?.let { product ->
            binding.editProductName.setText(product.productName)
            binding.editProductprice.setText(product.price.toString())
            binding.editProductDesc.setText(product.productDesc)
            Picasso.get().load(product.imageUrl).into(binding.imageBrowse)
        }
    }

    private fun uploadImage() {
        loadingUtils.show()
        imageUri?.let { uri ->
            productViewModel.uploadImage(this, uri) { imageUrl ->
                Log.d("ImageUpload", imageUrl.toString())
                if (imageUrl != null) {
                    if (isEditMode) {
                        updateProduct(imageUrl)
                    } else {
                        addProduct(imageUrl)
                    }
                } else {
                    Toast.makeText(this, "Failed to upload image", Toast.LENGTH_LONG).show()
                    loadingUtils.dismiss()
                }
            }
        }
    }

    private fun addProduct(url: String) {
        val productName = binding.editProductName.text.toString()
        val productPrice = binding.editProductprice.text.toString().toInt()
        val productDesc = binding.editProductDesc.text.toString()

        val product = ProductModel(
            "",
            productName,
            productDesc,
            productPrice,
            url
        )

        productViewModel.addProduct(product) { success, message ->
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            loadingUtils.dismiss()
            if (success) finish()
        }
    }

    private fun updateProduct(url: String?) {
        val updatedFields = mutableMapOf<String, Any>()
        updatedFields["productName"] = binding.editProductName.text.toString()
        updatedFields["price"] = binding.editProductprice.text.toString().toInt()
        updatedFields["productDesc"] = binding.editProductDesc.text.toString()
        url?.let {
            updatedFields["imageUrl"] = it
        }

        editingProduct?.let { product ->
            productViewModel.updateProduct(product.productId, updatedFields) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                loadingUtils.dismiss()
                if (success) finish()
            }
        }
    }
}
