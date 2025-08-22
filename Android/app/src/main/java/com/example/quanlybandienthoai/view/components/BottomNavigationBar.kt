package com.example.quanlybandienthoai.view.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.MainViewModel


@Composable
fun BottomNavigationBar(navController: NavController, viewModel: MainViewModel) {
    val selectedScreen = viewModel.selectedScreen.collectAsState()

    val listScreen by viewModel.screens.collectAsState()
//    listOf(Screen.Home, Screen.Shop, Screen.Profile)

    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Color.Gray
    ) {
        listScreen.forEach { screen ->
            val isSelected = screen == selectedScreen.value

            // Hiệu ứng scale khi chọn icon
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.3f else 1.0f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                label = ""
            )

            val color by animateColorAsState(
                targetValue = if (isSelected) Color.Blue else Color.Gray,
                animationSpec = tween(durationMillis = 300),
                label = ""
            )

            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon,
                        contentDescription = screen.title,
                        modifier = Modifier.size(24.dp * scale),
                        tint = color
                    )
                },
                label = { Text(screen.title, color = color) },
                selected = isSelected,
                selectedContentColor = Color.Blue,
                unselectedContentColor = Color.Gray,
                onClick = {
                    if (!isSelected) { // Tránh điều hướng lại chính nó
                        viewModel.selectScreen(screen)
//                        navController.navigate(screen.route) {
//                            popUpTo(navController.graph.startDestinationId) { saveState = true }
//                            launchSingleTop = true
//                            restoreState = true
//                        }
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) {
                                inclusive = false
                            } // Xóa mọi thứ sau Home
                            launchSingleTop = true
                        }

                    }
                }
            )
        }
    }
}
