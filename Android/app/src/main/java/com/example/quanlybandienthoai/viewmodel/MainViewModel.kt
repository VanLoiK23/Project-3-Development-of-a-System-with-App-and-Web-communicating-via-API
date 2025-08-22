package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.view.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(val context: Application) : AndroidViewModel(context) {
    private var _selectedScreen = MutableStateFlow<Screen>(Screen.Home)
    val selectedScreen: StateFlow<Screen> = _selectedScreen

    var _screens = MutableStateFlow(listOf(Screen.Home, Screen.Shop, Screen.Cart, Screen.Profile))


    var screens: StateFlow<List<Screen>> = _screens

    var showMenu by mutableStateOf(false)

    fun selectScreen(screen: Screen) {
        _selectedScreen.value = screen

        // Nếu màn hình chưa có trong danh sách bottom navigation, thêm vào
        if (screen !in _screens.value) {
            _screens.value += screen
        }

        if (screen !is Screen.ProductDetail) {
            _screens.value = _screens.value.filterNot { it is Screen.ProductDetail }
        }

        if (screen is Screen.Profile) {
            showMenu = true
        } else {
            showMenu = false
        }
    }

    fun deleteScreenInBottom(screen: Screen) {
        _screens.value = _screens.value.filterNot { it == screen }
    }

    fun updateSelectedScreen(navController: NavController) {
        val currentRoute = navController.currentDestination?.route ?: return
        _selectedScreen.value = when {
            currentRoute.startsWith("account") -> Screen.Profile
            currentRoute == "home" -> Screen.Home
            currentRoute == "shop" -> Screen.Shop
            currentRoute == "cart" -> Screen.Cart
            currentRoute == "profile" -> Screen.Profile

            currentRoute.startsWith("productManage") -> Screen.ManageProduct.Product
            currentRoute.startsWith("brandManage") -> Screen.ManageBrand.Brand
            currentRoute.startsWith("orderManage") -> Screen.ManageOrder.Order
            currentRoute.startsWith("userManage") -> Screen.ManageUser.User
            currentRoute == "homeManage" -> Screen.HomeAdmin

            else -> _selectedScreen.value
        }
    }


}
