package com.example.quanlybandienthoai.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.ImportExport
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ProductionQuantityLimits
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tag
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.Info
) {

    object Login : Screen(
        route = "login"
    )

    object Register : Screen(
        route = "register"
    )

    object Home : Screen("home", "Home", Icons.Default.Home)

    object Shop : Screen("shopping", "Shopping", Icons.Filled.ShoppingCart)

    data class ProductDetail(val productId: Int) :
        Screen("S/$productId", "Detail", Icons.Filled.Info)

    object Cart : Screen("cart", "Cart", Icons.Filled.Shop)

    object Profile : Screen("profile ", "Profile ", Icons.Filled.Person)

    object Checkout : Screen("checkout ", "Thanh toán ", Icons.Filled.Payment)

//    object Address : Screen("address", "Address", Icons.Filled.BookmarkAdd)
//
//    object InsertAddress : Screen("address_add", "Address", Icons.Filled.AddComment)
//
//    data class AddressDetails(val addressId: Int) :
//        Screen("S/$addressId", "Detail Address", Icons.Filled.Info)

    sealed class Account(route: String, title: String) :
        Screen(route, title, Icons.Default.Person) {
        object Main : Account("account", "Tài khoản")


        object Order : Account("account/order", "Đơn hàng")
        data class OrderDetail(val id: Int) :
            Account("account/order/detail/$id", "Chi tiết đơn hàng")
        data class ReviewProduct(val orderId: Int,val idSP: Int) :
            Account("account/order/review/$orderId/$idSP", "Đánh giá sản phẩm")


        object Address : Account("account/address", "Địa chỉ")
        object InsertAddress : Account("account/address/add", "Thêm địa chỉ")
        data class AddressDetail(val id: Int) :
            Account("account/address/detail/$id", "Chi tiết địa chỉ")


        object Post : Account("account/post", "Bài viết")
    }

    //screen admin

    //manage product
    sealed class ManageProduct(route: String, title: String) :
        Screen(route, title, Icons.Default.Inventory) {

        companion object {
            const val BASE_ROUTE = "productManage"
        }

        object Product : ManageProduct(
            route = BASE_ROUTE,
            title = "Quản lý sản phẩm"
        )

        data class ImportProduct(val id: Int) : ManageProduct(
            route = "$BASE_ROUTE/import/$id",
            title = "Nhập thêm sản phẩm"
        )

        object AddProduct : ManageProduct(
            route = "$BASE_ROUTE/add",
            title = "Thêm sản phẩm"
        )

        data class EditProduct(val id: Int) : ManageProduct(
            route = "$BASE_ROUTE/edit/$id",
            title = "Chỉnh sửa sản phẩm"
        )

        data class PreviewProduct(val id: Int) : ManageProduct(
            route = "$BASE_ROUTE/preview/$id",
            title = "Chi tiết sản phẩm"
        )
    }

    //category
    sealed class ManageBrand(route: String, title: String) :
        Screen(route, title, Icons.Filled.Label) {

        companion object {
            const val BASE_ROUTE = "brandManage"
        }

        object Brand : ManageBrand(
            route = BASE_ROUTE,
            title = "Quản lý thương hiệu"
        )

        data class upsertBrand(val id: Int) : ManageBrand(
            route = "$BASE_ROUTE/upsert/$id",
            title = "Thao tác với thương hiệu"
        )
    }

    //order
    sealed class ManageOrder(route: String, title: String) :
        Screen(route, title, Icons.Filled.Receipt) {

        companion object {
            const val BASE_ROUTE = "orderManage"
        }

        object Order : ManageOrder(
            route = BASE_ROUTE,
            title = "Quản lý đơn hàng"
        )

        data class PreviewOrder(val id: Int) : ManageOrder(
            route = "$BASE_ROUTE/preview/$id",
            title = "Chi tiết đơn hàng"
        )
    }

    //user
    sealed class ManageUser(route: String, title: String) :
        Screen(route, title, Icons.Filled.AccountCircle) {

        companion object {
            const val BASE_ROUTE = "userManage"
        }

        object User : ManageUser(
            route = BASE_ROUTE,
            title = "Quản lý khách hàng"
        )

        data class PreviewUser(val id: Int) : ManageUser(
            route = "$BASE_ROUTE/preview/$id",
            title = "Chi tiết khách hàng"
        )

        data class EditUser(val id: Int) : ManageUser(
            route = "$BASE_ROUTE/edit/$id",
            title = "Chỉnh sửa TT khách hàng"
        )
    }

    //home
    object HomeAdmin : Screen("homeManage", "Home", Icons.Default.HomeWork)

}

