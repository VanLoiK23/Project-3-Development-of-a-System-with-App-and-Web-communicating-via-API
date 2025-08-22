package com.example.quanlybandienthoai.view.screens.admin.sanpham

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.model.remote.entity.ImportProduct
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.IdentifyDialog
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import java.lang.reflect.Array

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductSelectionScreen(
    navController: NavController,
    id: Int,
    productViewModel: ProductManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(id) {
        mainViewModel.updateSelectedScreen(navController)
        productViewModel.setCurrentProduct(id)
        productViewModel.getSuppliers()
    }

    val product by productViewModel.product.collectAsState()
    val suppliers by productViewModel.suppliers.collectAsState()


    var selectedVariant by remember { mutableStateOf("") }
    var selectedSupplierName by remember { mutableStateOf("") }
    var selectedSupplierId by remember { mutableLongStateOf(0) }
    var selectedId by remember { mutableIntStateOf(0) }
    var selectedPrice by remember { mutableIntStateOf(0) }

    var quantity by remember { mutableStateOf("1") }
    var dropdownExpanded by remember { mutableStateOf(false) }
    var dropdownExpanded1 by remember { mutableStateOf(false) }
    var showImeiInput by remember { mutableStateOf(false) }
    var imeiError by remember { mutableStateOf(false) }

    val imeiInputs = remember { mutableStateListOf<MutableState<String>>() }

//    val variants = listOf(
//        mapOf(
//            "id" to 1,
//            "ram" to "4GB",
//            "rom" to "256GB",
//            "color" to "Hổ",
//            "price" to "10,000,000đ"
//        ),
//        mapOf(
//            "id" to 2,
//            "ram" to "8GB",
//            "rom" to "512GB",
//            "color" to "Xám",
//            "price" to "15,000,000đ"
//        )
//    )

    val variants = product.pbspList.map { pb ->
        mapOf(
            "id" to pb.maphienbansp,
            "ram" to if (pb.ram == null) {
                null
            } else {
                pb.ram + "GB"
            },
            "rom" to if (pb.rom == null) {
                null
            } else {
                pb.rom + "GB"
            },
            "color" to pb.mausac,
            "price" to pb.gianhap
        )
    }

    val variantList = remember { mutableStateListOf<MutableMap<String, Any>>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Nhập thêm sản phẩm " + product.tensp,
                            fontSize = 15.sp,
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        productViewModel.isExit.value = true
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF7D32A8),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Text(
                "Lựa chọn phiên bản sản phẩm",
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 8.dp, top = 10.dp),
                fontFamily = fontFamily(),
                fontWeight = FontWeight.ExtraBold
            )

            ExposedDropdownMenuBox(
                expanded = dropdownExpanded,
                onExpandedChange = { dropdownExpanded = !dropdownExpanded }
            ) {
                TextField(
                    value = selectedVariant,
                    onValueChange = {},
                    label = { Text("Chọn phiên bản sản phẩm") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded)
                    },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    variants.forEach { variant ->
                        val price = (variant["price"] as? Number)?.toDouble() ?: 0.0

                        val display =
                            "${variant["ram"]} - ${variant["rom"]} - ${variant["color"]} (${
                                formatCurrency(price)
                            })"
                        DropdownMenuItem(
                            text = { Text(display) },
                            onClick = {
                                selectedVariant = display
                                selectedId = variant["id"] as? Int ?: 0
                                selectedPrice = price.toInt()

                                dropdownExpanded = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it },
                    label = { Text("Số lượng") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .width(120.dp)
                        .height(54.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.Blue
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (selectedVariant.isNotEmpty() && quantity.toIntOrNull() != null) {
                            val existingItem = variantList.find { it["variant"] == selectedVariant }
                            if (existingItem != null) {
                                existingItem["quantity"] =
                                    (existingItem["quantity"] as Int) + quantity.toInt()
                            } else {
                                variantList.add(
                                    mutableMapOf(
                                        "variant" to selectedVariant,
                                        "quantity" to quantity.toInt(),
                                        "id" to selectedId,
                                        "price" to selectedPrice
                                    )
                                )
                                imeiInputs.add(mutableStateOf(""))
                            }
                            quantity = "1"
                        }
                    }
                ) {
                    Text("Thêm")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (variantList.isNotEmpty()) {
                Text(
                    "Các phiên bản đã chọn",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .background(Color(0xFFF5F5F5))
                        .padding(8.dp)
                ) {
                    // Header
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        listOf("STT", "RAM", "ROM").forEach {
                            Text(
                                it,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                                fontFamily = fontFamily(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        listOf("Màu", "Giá").forEach {
                            Text(
                                it,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(2f),
                                fontFamily = fontFamily(),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            "Số lượng",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Xóa",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f),
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Content
                    variantList.forEachIndexed { index, item ->
                        val split = (item["variant"] as String).split(" - ", "(", ")")
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "${index + 1}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(0.5f)
                            )
                            Text(
                                split.getOrNull(0)?.trim() ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                split.getOrNull(1)?.trim() ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                split.getOrNull(2)?.trim() ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                split.getOrNull(3)?.trim() ?: "",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(2f)
                            )
                            Text(
                                "${item["quantity"]}",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = {
                                    variantList.removeAt(index)
                                    imeiInputs.removeAt(index)
                                },
                                modifier = Modifier.weight(0.5f)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    "Lựa chọn nhà cung cấp",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp, top = 5.dp),
                    fontFamily = fontFamily(),
                    fontWeight = FontWeight.ExtraBold
                )


                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(5.dp)
                ) {
                    Column(
                        modifier = Modifier.weight(2f),
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = dropdownExpanded1,
                            onExpandedChange = { dropdownExpanded1 = !dropdownExpanded1 }
                        ) {
                            TextField(
                                value = selectedSupplierName,
                                onValueChange = {},
                                label = { Text("Chọn nhà cung cấp") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded1)
                                },
                                readOnly = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = dropdownExpanded1,
                                onDismissRequest = { dropdownExpanded1 = false }
                            ) {
                                suppliers.forEach { supplier ->
                                    val display = supplier.tenNhaCungCap
                                    DropdownMenuItem(
                                        text = { Text(display) },
                                        onClick = {
                                            selectedSupplierName = display
                                            selectedSupplierId = supplier.id

                                            dropdownExpanded1 = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                        if (showImeiInput && selectedSupplierName.isEmpty()) {
                            Text(
                                text = "Vui lòng chọn nhà cung cấp",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 16.sp,
                                color = Color.Red
                            )
                        }
                    }

                    Button(
                        onClick = { showImeiInput = true },
//                        modifier = Modifier.align(Alignment.End)
                        modifier = Modifier.weight(1f),

                        ) {
                        Text("Thêm IMEI")
                    }
                }
            }

            // IMEI Input Dialog
            if (showImeiInput && selectedSupplierName.isNotEmpty()) {
                AlertDialog(
                    onDismissRequest = { showImeiInput = false },
                    title = { Text("Nhập IMEI cho sản phẩm") },
                    text = {
                        Column(
                            Modifier.verticalScroll(rememberScrollState())
                        ) {
                            variantList.forEachIndexed { variantIndex, item ->
                                Text(
                                    text = "IMEI cho ${item["variant"]}",
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )

                                TextField(
                                    value = imeiInputs.getOrNull(variantIndex)?.value ?: "",
                                    onValueChange = { newValue ->
                                        if (imeiInputs.size <= variantIndex) {
                                            imeiInputs.add(mutableStateOf(newValue))
                                        } else {
                                            imeiInputs[variantIndex].value = newValue
                                        }
                                    },
                                    label = { Text("IMEI") },
                                    isError = imeiError && (imeiInputs.getOrNull(variantIndex)?.value?.length != 15),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                )

                            }


                            if (imeiError) {
                                Text(
                                    text = "Vui lòng nhập đủ 15 số cho mỗi IMEI.",
                                    color = Color.Red,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            imeiError = imeiInputs.any { it.value.length != 15 }
                            if (!imeiError) {
                                productViewModel.isConfirm.value = true
                            }
                        }) {
                            Text("Xác nhận")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showImeiInput = false
                            imeiError = false
                        }) {
                            Text("Hủy")
                        }
                    }
                )
            }

            IdentifyDialog(
                openDialog = productViewModel.isExit,
                "Bạn có muốn thoát !!",
                "Những thay đổi sẽ không được lưu ?",
                "Confirm"
            ) {
                productViewModel.isExit.value = false

                variantList.clear()
                imeiInputs.clear()

                navController.popBackStack()
            }

            IdentifyDialog(
                openDialog = productViewModel.isConfirm,
                "Nhập thêm sản phẩm !!",
                "Bạn có muốn nhập thêm sản phẩm ?",
                "Confirm"
            ) {
                productViewModel.isConfirm.value = false

                showImeiInput = false

                val ids = mutableListOf<Int>()
                val prices = mutableListOf<Int>()
                val quantities = mutableListOf<Int>()
                val imeis = mutableListOf<Long>()
                var totalQuantity = 0
                val supplierId = selectedSupplierId

                variantList.forEachIndexed { index, variantMap ->
                    ids.add(variantMap["id"] as Int)
                    prices.add((variantMap["price"] as? Number)?.toInt() ?: 0)
                    quantities.add(variantMap["quantity"] as Int)
                    totalQuantity += variantMap["quantity"] as Int

                    imeiInputs.getOrNull(index)?.value?.toLongOrNull()?.let {
                        imeis.add(it)
                    }
                }

                val importProduct = ImportProduct(ids, prices, quantities, imeis, supplierId)
                productViewModel.importProduct(importProduct, id, totalQuantity.toDouble())

//                var listVersionIdAndQuantity: ArrayList<Map<Int, Int>> = ArrayList()
//                ids.forEachIndexed { index, id ->
//                    listVersionIdAndQuantity.add(mapOf(id to quantities[index]))
//                }

                val listVersionIdAndQuantity = arrayListOf<Map<Int, Int>>() // hoặc mutableListOf()
                ids.forEachIndexed { index, id ->
                    listVersionIdAndQuantity.add(mapOf(id to quantities[index]))
                }

                Log.d("TestImport",listVersionIdAndQuantity.toString())

                listVersionIdAndQuantity.forEach { item ->
                    productViewModel.importProductUpdateInFireBase(
                        item
                    )
                }


                variantList.clear()
                imeiInputs.clear()
                navController.popBackStack()

                productViewModel.isImport.value = true
            }
        }
    }
}
