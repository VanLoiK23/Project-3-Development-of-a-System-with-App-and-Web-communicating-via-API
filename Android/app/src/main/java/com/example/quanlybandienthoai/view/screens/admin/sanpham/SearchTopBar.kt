package com.example.quanlybandienthoai.view.screens.admin.sanpham

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.SearchBarState
import com.example.quanlybandienthoai.view.components.TopBar
import com.example.quanlybandienthoai.view.screens.home.SearchHomeTopBar
import com.example.quanlybandienthoai.viewmodel.HomeViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel

@Composable
fun SearchTopBar(
    navController: NavController,
    homeViewModel: ProductManageViewModel,
    onClick: (String) -> Unit
) {
    val context = LocalContext.current
    val searchBarState by homeViewModel.searchBarState
    val searchTextState by homeViewModel.searchTextState
    val homeVM = viewModel<HomeViewModel>(LocalContext.current as ComponentActivity)

    when (searchBarState) {
        SearchBarState.CLOSED -> {
            TopBar(
                title = "Quản lý sản phẩm",
                false,
                actions = {
                    androidx.compose.material.IconButton(onClick = {
                        homeViewModel.updateSearchBarState(
                            newValue = SearchBarState.OPENED
                        )
                    }) {
                        androidx.compose.material.Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Tìm kiếm"
                        )
                    }

                }, navBack = {
                    navController.popBackStack()
                }
            )
        }

        SearchBarState.OPENED -> {
            SearchHomeTopBar(text = searchTextState,
                onTextChange = { homeViewModel.updateSearchTextState(newValue = it) },
                onCloseClicked = {
                    homeViewModel.updateSearchBarState(newValue = SearchBarState.CLOSED)
                    homeViewModel.getProducts()
                },
                onSearchClicked = {
                    if (searchTextState == "") {
                        Toast.makeText(
                            context,
                            "Vui lòng điền tên sản phẩm muốn tìm kiếm",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        homeVM.actionType = "search"
                        onClick(searchTextState)
                    }
                })
        }
    }
}