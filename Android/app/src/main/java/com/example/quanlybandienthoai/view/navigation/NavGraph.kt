package com.example.quanlybandienthoai.view.navigation


import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.example.quanlybandienthoai.view.RegisterScreen
import com.example.quanlybandienthoai.view.screens.admin.category.CategoryManagementScreen
import com.example.quanlybandienthoai.view.screens.admin.category.UpsertCategory
import com.example.quanlybandienthoai.view.screens.admin.home.DashboardScreen
import com.example.quanlybandienthoai.view.screens.admin.order.OrderDetailManageScreen
import com.example.quanlybandienthoai.view.screens.admin.order.OrderManagementScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.AddProductScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.EditProductScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.PreviewScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductManagementScreen
import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductSelectionScreen
import com.example.quanlybandienthoai.view.screens.admin.user.EditAccountScreen
import com.example.quanlybandienthoai.view.screens.admin.user.ThongTinKhachHangScreen
import com.example.quanlybandienthoai.view.screens.admin.user.UserManagementScreen
import com.example.quanlybandienthoai.view.screens.profile.address.AddressScreen
import com.example.quanlybandienthoai.view.screens.profile.address.InsertAddress
import com.example.quanlybandienthoai.view.screens.cart.ShoppingCart
import com.example.quanlybandienthoai.view.screens.checkout.CheckoutScreen
import com.example.quanlybandienthoai.view.screens.home.HomeScreen
import com.example.quanlybandienthoai.view.screens.login.LoginScreen
import com.example.quanlybandienthoai.view.screens.product.ProductDetailsScreen
import com.example.quanlybandienthoai.view.screens.product.ProductList
import com.example.quanlybandienthoai.view.screens.profile.ProfileScreen
import com.example.quanlybandienthoai.view.screens.profile.account.AccountScreen
import com.example.quanlybandienthoai.view.screens.profile.address.UpdateAddress
import com.example.quanlybandienthoai.view.screens.profile.order.OrderDetailScreen
import com.example.quanlybandienthoai.view.screens.profile.order.OrderScreen
import com.example.quanlybandienthoai.view.screens.profile.order.ProductReviewScreen
import com.example.quanlybandienthoai.viewmodel.CartViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.HomeViewModelFactory
import com.google.accompanist.navigation.animation.composable


@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController, modifier: Modifier, homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {


        composable(
            route = Screen.Login.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Register.route) {
                    // Từ Register quay lại Login (sang phải)
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(500)
                    )
                } else {
                    // Mặc định: sang trái
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(500)
                    )
                }
            },
            exitTransition = {
                if (targetState.destination.route == Screen.Register.route) {
                    // Đi đến Register (sang trái)
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(
                        targetScale = 1.1f,
                        animationSpec = tween(500)
                    )
                } else {
                    // Mặc định: sang phải
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(
                        targetScale = 1.1f,
                        animationSpec = tween(500)
                    )
                }
            }
        ) {
            LoginScreen(onSuccessfulLogin = { isAdmin ->
                navController.popBackStack()

                if (isAdmin) {
                    navController.navigate(Screen.HomeAdmin.route)
                } else {
                    navController.navigate(Screen.Home.route)
                }
            }, onRegisterClick = {
                navController.navigate(Screen.Register.route)
            })
        }

        composable(
            route = Screen.Register.route,
            enterTransition = {
                if (initialState.destination.route == Screen.Login.route) {
                    // Từ Login sang Register (sang trái)
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(500)
                    )
                } else {
                    // Mặc định: sang phải
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    ) + fadeIn(animationSpec = tween(500)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(500)
                    )
                }
            },
            exitTransition = {
                if (targetState.destination.route == Screen.Login.route) {
                    // Quay lại Login (sang phải)
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(
                        targetScale = 1.1f,
                        animationSpec = tween(500)
                    )
                } else {
                    // Mặc định: sang trái
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(500)
                    ) + fadeOut(animationSpec = tween(300)) + scaleOut(
                        targetScale = 1.1f,
                        animationSpec = tween(500)
                    )
                }
            }
        ) {
            RegisterScreen(navToLogin = {
                navController.popBackStack()
                navController.navigate(Screen.Login.route) { launchSingleTop = true }
            })
        }


        composable(route = Screen.Home.route) {
            HomeScreen(navController, homeViewModel)
        }
//        composable(Screen.Profile.route) { ProfileScreen() }
//        composable(Screen.Settings.route) { SettingsScreen() }

        composable("shopping") {
            ProductList(navController, homeViewModel)
        }
//
//
//        // Màn hình chi tiết sản phẩm, nhận productId làm tham số
        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: 0
            ProductDetailsScreen(productId, navController, homeViewModel)
        }

        composable("cart") {
            ShoppingCart(navController)
        }

        composable(Screen.Checkout.route) {
            CheckoutScreen(navController)
        }


        //Manage Info account,order,address,Post watch
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        //Account
        composable(Screen.Account.Main.route) {
            AccountScreen(navController)
        }
        //Order
        composable(Screen.Account.Order.route) {
            OrderScreen(navController)
        }
        composable(
            "account/order/detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            OrderDetailScreen(navController, id)
        }
        composable(
            "account/order/review/{orderId}/{idSP}",
            arguments = listOf(
                navArgument("orderId") { type = NavType.IntType },
                navArgument("idSP") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val orderId = backStackEntry.arguments?.getInt("orderId") ?: return@composable
            val productId = backStackEntry.arguments?.getInt("idSP") ?: return@composable
            ProductReviewScreen(navController, orderId, productId)
        }

        //Address
        composable(Screen.Account.Address.route) {
            AddressScreen(navController)
        }
        composable(Screen.Account.InsertAddress.route) {
            InsertAddress(navController)
        }
        composable(
            "account/address/detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable
            UpdateAddress(navController, id)
        }


        //admin navigate

        //manage product
        composable(
            "productManage/import/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            ProductSelectionScreen(navController, id)
        }

        composable(
            "productManage/preview/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            PreviewScreen(navController, id)
        }

        composable(Screen.ManageProduct.Product.route) {
            ProductManagementScreen(navController)
        }

        composable(Screen.ManageProduct.AddProduct.route) {
            AddProductScreen(navController)
        }

        composable(
            "productManage/edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            EditProductScreen(navController, id)
        }

        //manage brand
        composable(
            "brandManage/upsert/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            UpsertCategory(navController, id)
        }

        composable(Screen.ManageBrand.Brand.route) {
            CategoryManagementScreen(navController)
        }

        //manage order
        composable(
            "orderManage/preview/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            OrderDetailManageScreen(navController,id)
        }

        composable(Screen.ManageOrder.Order.route) {
            OrderManagementScreen(navController)
        }


        //manage user
        composable(
            "userManage/preview/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            ThongTinKhachHangScreen(navController,id)
        }
        composable(Screen.ManageUser.User.route) {
            UserManagementScreen(navController)
        }
        composable(
            "userManage/edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: return@composable

            EditAccountScreen(navController,id)
        }

        //home admin
        composable(Screen.HomeAdmin.route) {
            DashboardScreen(navController)
        }

    }
}
