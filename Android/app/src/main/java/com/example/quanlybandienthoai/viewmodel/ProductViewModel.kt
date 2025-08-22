package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.model.repository.ReviewRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductViewModel(val context: Application) : AndroidViewModel(context) {
    private val productRepository = ProductRepository
    private val reviewRepository = ReviewRepository

    //    private val cartRepository = CartRepository
//    private val orderRepository = OrderRepository
    var isLoadingDetail by mutableStateOf(false)


    private val _product = MutableStateFlow(Sanpham()) // Giá trị mặc định
    val product: StateFlow<Sanpham> = _product


    private val _products = MutableStateFlow<List<Sanpham>>(emptyList())
    val products: StateFlow<List<Sanpham>> = _products

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            _products.value = productRepository.getAllProducts()
        }
    }


    fun setCurrentProduct(productId: String) {
        isLoadingDetail = true
        if (productId.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                _product.value = Sanpham()
                val tempProduct = productRepository.getProductById(productId) ?: Sanpham()
                _product.value = tempProduct // Cập nhật sản phẩm trực tiếp vào StateFlow
            }
        }
        isLoadingDetail = false
    }

    var isLoadingComment by mutableStateOf(false)

    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    fun getReviewsByProduct(productId: Int) {
        isLoadingComment = true
        viewModelScope.launch(Dispatchers.IO) {
            _reviews.value = emptyList()
            _reviews.value = reviewRepository.getAllReviewsWithUsers(productId)
        }
        isLoadingComment = false
    }



//    fun addToCart(userId: String, productId: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            cartRepository.insertCart(Cart(uid = userId, pid = productId, quantity = 1))
//        }
//    }


//    fun userBoughtItem(userId: String, productId: String): Boolean {
//        var userBoughtIt = false
//        runBlocking {
//            this.launch(Dispatchers.IO) {
//                userBoughtIt = orderRepository.userBoughtItem(userId, productId)
//            }
//        }
//        return userBoughtIt
//    }
}