package com.example.quanlybandienthoai.view.screens.admin.sanpham

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.model.remote.entity.RequestProductInteraction
import com.example.quanlybandienthoai.view.components.IdentifyDialog
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.home.formatCurrency
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.ProductManageViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    navController: NavController,
    id: Int,
    viewModel: ProductManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val productName by viewModel.productName.collectAsState()
    val mainImage by viewModel.mainImageUri.collectAsState()
    val subImages by viewModel.subImageUris.collectAsState()

    val coroutineScope = rememberCoroutineScope()


    val rams by viewModel.ram.collectAsState()
    val roms by viewModel.rom.collectAsState()
    val colors by viewModel.color.collectAsState()
    val xx by viewModel.xx.collectAsState()
    val hdh by viewModel.hdh.collectAsState()
    val kho by viewModel.kho.collectAsState()
    val brands by viewModel.brands.collectAsState()

    var isNewMainImage = false
    var isNewSubImages = mutableListOf<Boolean>()


    val productVariants = remember { mutableStateListOf<ProductVariant>() }

    var selectedMainImage by remember { mutableStateOf<Uri?>(mainImage) }
    val pickMainImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedMainImage = it
                viewModel.mainImageUri.value = it
                viewModel.isSelectImg.value = true
                isNewMainImage = true

            }
        }

    var subImageList by remember { mutableStateOf(subImages) }
    val pickSubImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            subImageList = uris.take(3)
            viewModel.isSelectImg.value = true
            isNewSubImages = MutableList(uris.size) { true }

        }


    val product by viewModel.product.collectAsState()


    LaunchedEffect(Unit) {
        if (rams.isEmpty()) {
            viewModel.initData()
        }
    }


    // Lần đầu load id
    LaunchedEffect(id) {
        mainViewModel.updateSelectedScreen(navController)
        viewModel.setCurrentProduct(id)
        viewModel.reset()
        selectedMainImage = null
        subImageList = emptyList()
        productVariants.clear()
    }

// Khi product đã load xong
    LaunchedEffect(product) {
        if (product.isNotNull()) {
            viewModel.initProductEdit()

            val imageUrls = product.hinhanh.split(",")

            selectedMainImage = imageUrls.getOrNull(0)?.toUri()
            subImageList = imageUrls.drop(1).map { it.toUri() }
            isNewMainImage = false
            isNewSubImages = MutableList(subImageList.size) { false }

            productVariants.clear()
            product.pbspList.forEach { pb ->
                productVariants.add(
                    ProductVariant(
                        pb.ram + " GB",
                        pb.rom + " GB",
                        pb.mausac,
                        pb.gianhap.toInt().toString(),
                        pb.giaxuat.toInt().toString(),
                        existingVariantIds = pb.maphienbansp
                    )
                )
            }
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            "Chỉnh sửa sản phẩm "+product.tensp,
                            fontSize = 15.sp,
                            fontFamily = fontFamily(),
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.isExit.value = true
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
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {

            suspend fun uploadAllImages(context: Context): List<String> {
                viewModel.isUploading = true
                Log.d("AAA", "Running")
                val result = mutableListOf<String>()

                // Ảnh chính
                val mainImageUrl = if (isNewMainImage) {
                    viewModel.uploadImageSuspend(
                        context,
                        selectedMainImage!!,
                        "${viewModel.productName.value}1"
                    )
                } else {
                    selectedMainImage.toString()
                }
                mainImageUrl?.let { result.add(it) }

                // Ảnh phụ
                subImageList.forEachIndexed { index, uri ->
                    val url = if (isNewSubImages.getOrNull(index) == true) {
                        viewModel.uploadImageSuspend(
                            context,
                            uri,
                            "${viewModel.productName.value}${index + 2}"
                        )
                    } else {
                        uri.toString()
                    }
                    url?.let { result.add(it) }
                }

                return result
            }


            suspend fun handleAdd() {
                try {
                    if (viewModel.validateInput()) {
                        viewModel.errorMessage = ""

//                        if (viewModel.isSelectImg.value) {
                            viewModel.img.value =
                                uploadAllImages(context).joinToString(separator = ",")


//                    viewModel.description.value = "<p>${viewModel.description.value}</p>"

                        viewModel.selectedXXID.value =
                            xx.firstOrNull { it.tenXuatXu == viewModel.selectedXX.value }?.id ?: -1
                        viewModel.selectedHDHID.value =
                            hdh.firstOrNull { it.tenHeDieuHanh == viewModel.selectedHDH.value }?.id
                                ?: -1
                        viewModel.selectedTHID.value =
                            brands.firstOrNull { it.tenthuonghieu == viewModel.selectedTH.value }?.mathuonghieu
                                ?: -1
                        viewModel.selectedWarehouseID.value =
                            kho.firstOrNull { it.tenKhuVuc == viewModel.selectedWarehouse.value }?.id
                                ?: -1


                        val request = RequestProductInteraction(
                            productName,
                            viewModel.img.value,
                            viewModel.selectedXXID.value,
                            viewModel.selectedHDHID.value,
                            viewModel.selectedTHID.value,
                            viewModel.selectedWarehouseID.value,
                            viewModel.giamgia.value.toInt(),
                            viewModel.giare.value.toInt(),
                            viewModel.dungluongpin.value.toInt(),
                            viewModel.cameratruoc.value,
                            viewModel.camerasau.value,
                            viewModel.phienbanHDH.value.toInt(),
                            viewModel.manhinh.value,
                            viewModel.title.value,
                            viewModel.description.value,
                            viewModel.isTragop.value,
                            listPB = productVariants.toList(),
                            id = id
                        )
                        Log.d("AAA", "$request")
                        viewModel.updateProduct(request)

                        if (!viewModel.isUploading) {
                            viewModel.reset()

                            selectedMainImage = null
                            subImageList = emptyList()
                            productVariants.clear()
                            viewModel.isSelectImg.value = false

                            viewModel.isUpsert.value = true

                            navController.popBackStack()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("HandleAdd", "Lỗi khi lưu sản phẩm", e)
                }
            }
            Spacer(Modifier.height(20.dp))


            OutlinedTextField(
                value = productName,
                onValueChange = {
                    if (viewModel.productNameErr) viewModel.productNameErr = false
                    viewModel.productName.value = it
                },
                label = { Text("Tên sản phẩm") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.productNameErr
            )

            Spacer(Modifier.height(8.dp))
            Text("Ảnh chính:")
            // Ảnh chính
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clickable { pickMainImageLauncher.launch("image/*") }
                    .clip(RoundedCornerShape(24.dp)) // clip để ảnh bo tròn
                    .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                selectedMainImage?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Icon(
                    imageVector = Icons.Default.Image,
                    contentDescription = "Chọn ảnh",
                    tint = Color.Gray,
                    modifier = Modifier.size(64.dp)
                )
            }

            // Ảnh phụ
            Spacer(Modifier.height(8.dp))
            Text("Ảnh phụ (tối đa 3 ảnh):")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                subImageList.forEach { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(16.dp)) // bo ảnh phụ luôn
                            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                    )
                }
                IconButton(
                    onClick = { pickSubImageLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Thêm ảnh")
                }
            }

            Spacer(Modifier.height(13.dp))


            // 2 cột: Khuyến mãi và giảm giá trực tiếp
            Row(horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                OutlinedTextField(
                    value = viewModel.giare.value,
                    onValueChange = {
                        if (viewModel.giareErr) viewModel.giareErr = false

                        viewModel.giare.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text("Khuyến mãi(%)") },
                    modifier = Modifier.weight(1f),
                    isError = viewModel.giareErr
                )
                OutlinedTextField(
                    value = viewModel.giamgia.value,
                    onValueChange = {
                        if (viewModel.giamgiaErr) viewModel.giamgiaErr = false

                        viewModel.giamgia.value = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    label = { Text("Giảm giá trực tiếp(%)") },
                    modifier = Modifier.weight(1f),
                    isError = viewModel.giamgiaErr
                )
            }

//        var selectedXX by remember { mutableStateOf("") }
//        var selectedHDH by remember { mutableStateOf("") }
//        var selectedTH by remember { mutableStateOf("") }
            // Dropdowns: Xuất xứ, HĐH, Thương hiệu
            DropdownField(
                label = "Xuất xứ",
                viewModel.selectedXX.value,
                options = xx.map { it.tenXuatXu }
            ) { option ->
                viewModel.selectedXX.value = option
            }
            DropdownField(
                label = "Hệ điều hành",
                viewModel.selectedHDH.value,
                options = hdh.map { it.tenHeDieuHanh } + listOf("Không")
            ) { option ->
                viewModel.selectedHDH.value = option
            }
            DropdownField(
                label = "Thương hiệu",
                viewModel.selectedTH.value,
                options = brands.map { it.tenthuonghieu }
            ) { option ->
                viewModel.selectedTH.value = option
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = viewModel.isTragop.value, onCheckedChange = {
                    viewModel.isTragop.value = it
                })
                Text("Trả góp 0%")
            }

            // Thông số kỹ thuật
            OutlinedTextField(
                value = viewModel.dungluongpin.value,
                onValueChange = {
                    if (viewModel.dungluongpinErr) viewModel.dungluongpinErr = false
                    viewModel.dungluongpin.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                label = { Text("Dung lượng pin") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.dungluongpinErr
            )

            OutlinedTextField(
                value = viewModel.camerasau.value,
                onValueChange = {
                    if (viewModel.camerasauErr) viewModel.camerasauErr = false
                    viewModel.camerasau.value = it
                },
                label = { Text("Camera sau") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.camerasauErr
            )

            OutlinedTextField(
                value = viewModel.cameratruoc.value,
                onValueChange = {
                    if (viewModel.cameratruocErr) viewModel.cameratruocErr = false
                    viewModel.cameratruoc.value = it
                },
                label = { Text("Camera trước") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.cameratruocErr
            )

            OutlinedTextField(
                value = viewModel.manhinh.value,
                onValueChange = {
                    if (viewModel.manhinhErr) viewModel.manhinhErr = false
                    viewModel.manhinh.value = it
                },
                label = { Text("Màn hình") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.manhinhErr
            )

            OutlinedTextField(
                value = viewModel.phienbanHDH.value,
                onValueChange = {
                    if (viewModel.phienbanHDHErr) viewModel.phienbanHDHErr = false
                    viewModel.phienbanHDH.value = it
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                label = { Text("Phiên bản hệ điều hành") },
                modifier = Modifier.fillMaxWidth(),
                isError = viewModel.phienbanHDHErr
            )


            DropdownField(
                label = "Khu vực kho",
                viewModel.selectedWarehouse.value,
                options = kho.map { it.tenKhuVuc }
            ) { option ->
                viewModel.selectedWarehouse.value = option
            }

            var selectedRam by remember { mutableStateOf("") }
            var selectedRom by remember { mutableStateOf("") }
            var selectedColor by remember { mutableStateOf("") }
            var inputImportPrice by remember { mutableStateOf("") }
            var inputExportPrice by remember { mutableStateOf("") }


            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            val ramOptions = rams.map { "${it.kichThuocRam} GB" } + listOf("Không")
            val romOptions = roms.map { "${it.kichThuocRom} GB" } + listOf("Không")
            val colorOptions = colors.map { it.tenMauSac }


            var ramExpanded by remember { mutableStateOf(false) }
            var romExpanded by remember { mutableStateOf(false) }
            var colorExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = ramExpanded,
                onExpandedChange = { ramExpanded = !ramExpanded }
            ) {
                TextField(
                    value = selectedRam,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chọn RAM") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = ramExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = ramExpanded,
                    onDismissRequest = { ramExpanded = false }
                ) {
                    ramOptions.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            selectedRam = it
                            ramExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = romExpanded,
                onExpandedChange = { romExpanded = !romExpanded }
            ) {
                TextField(
                    value = selectedRom,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chọn ROM") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = romExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = romExpanded,
                    onDismissRequest = { romExpanded = false }
                ) {
                    romOptions.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            selectedRom = it
                            romExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = colorExpanded,
                onExpandedChange = { colorExpanded = !colorExpanded }
            ) {
                TextField(
                    value = selectedColor,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Chọn màu sắc") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = colorExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = colorExpanded,
                    onDismissRequest = { colorExpanded = false }
                ) {
                    colorOptions.forEach {
                        DropdownMenuItem(text = { Text(it) }, onClick = {
                            selectedColor = it
                            colorExpanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = inputImportPrice,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { inputImportPrice = it },
                    label = { Text("Giá nhập") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = inputExportPrice,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { inputExportPrice = it },
                    label = { Text("Giá xuất") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if (selectedRam.isNotBlank() && selectedRom.isNotBlank() && selectedColor.isNotBlank()) {
                    productVariants.add(
                        ProductVariant(
                            selectedRam,
                            selectedRom,
                            selectedColor,
                            inputImportPrice,
                            inputExportPrice
                        )
                    )
                    selectedRam = ""
                    selectedRom = ""
                    selectedColor = ""
                    inputImportPrice = ""
                    inputExportPrice = ""
                }
            }) {
                Text("Thêm phiên bản")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Danh sách phiên bản", fontWeight = FontWeight.Bold)
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("RAM", "ROM", "Màu")
                    .forEach { label ->
                        Text(
                            label,
                            modifier = Modifier.weight(1f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                listOf("Giá nhập", "Giá xuất")
                    .forEach { label ->
                        Text(
                            label,
                            modifier = Modifier.weight(2f),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                Text("", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold)

            }
            Divider()
            LazyColumn(modifier = Modifier.heightIn(max = 500.dp)) {
                itemsIndexed(productVariants) { index, variant ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(
                            variant.ram,
                            variant.rom,
                            variant.color,
                        ).forEach {
                            Text(it, modifier = Modifier.weight(1f))
                        }

                        listOf(
                            variant.importPrice,
                            variant.exportPrice
                        ).forEach {
                            Text(formatCurrency(it.toDouble()), modifier = Modifier.weight(2f))
                        }

                        IconButton(onClick = { productVariants.removeAt(index) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Xóa")
                        }
                    }
                    Divider()
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            ProductDescriptions(
                shortDescription = viewModel.title.value,
                onShortDescChange = {
                    if (viewModel.titleErr) viewModel.titleErr = false
                    viewModel.title.value = it
                },
                detailedDescription = viewModel.description.value,
                onDetailedDescChange = { viewModel.description.value = it },
                viewModel,
                false
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.errorMessage.isNotEmpty()) {
                Text(
                    text = viewModel.errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        viewModel.isExit.value = true
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Quay lại")
                }

                Button(
                    onClick = {
                        coroutineScope.launch {
                            handleAdd()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                    enabled = !viewModel.isUploading
                ) {
                    if (viewModel.isUploading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Lưu thay đổi")
                    }
                }
            }
        }

        IdentifyDialog(
            openDialog = viewModel.isExit,
            "Bạn có muốn thoát !!",
            "Những thay đổi sẽ không được lưu ?",
            "Confirm"
        ) {
            viewModel.isExit.value = false

            viewModel.reset()
            selectedMainImage = null
            subImageList = emptyList()
            productVariants.clear()

            navController.popBackStack()
        }
    }
}
