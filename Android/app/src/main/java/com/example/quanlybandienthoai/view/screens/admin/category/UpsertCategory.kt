package com.example.quanlybandienthoai.view.screens.admin.category

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.view.screens.profile.order.generateRandomCode
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.admin.CategoryManageViewModel
import kotlinx.coroutines.launch

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertCategory(
    navController: NavHostController,
    id: Int,
    viewModel: CategoryManageViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    LaunchedEffect(id) {
        viewModel.setCurrentBrand(id)
    }
    val brand by viewModel.brand.collectAsState()

    var name by remember { mutableStateOf(viewModel.name.value) }
    var img by remember { mutableStateOf(viewModel.img.value) }

    var isSuccess by remember { mutableStateOf(false) }

    var selectedImage by remember { mutableStateOf(viewModel.selectedImageUri) }
    val context = LocalContext.current

    LaunchedEffect(brand) {
        if (brand.tenthuonghieu.isNotEmpty()) {
            name = brand.tenthuonghieu
            img = brand.image
        }
    }

    if (id == -1) {
        name = ""
        img = ""
    }

    val coroutineScope = rememberCoroutineScope()


    val pickImageLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImage = it
            }
        }


    Scaffold(topBar = {
        TopAppBar(title = {

            Text(
                text = "Thao tác với thương hiệu ", fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = fontFamily(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

        }, navigationIcon = {
            IconButton(onClick = {

                navController.popBackStack()

                mainViewModel.updateSelectedScreen(navController)
            }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFFF5722)
                )
            }
        }, colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
        )
    }) { paddingValue ->

        fun handleReviewClick() {

            if (viewModel.validateFormCategory()) {
                viewModel.messageError = ""

                if (selectedImage != null) {
                    viewModel.uploadImage(
                        context, selectedImage!!,
                        nameImage = generateRandomCode()
                    ) { srtImg ->
                        viewModel.img.value = srtImg

                        val saveBrand = Thuonghieu(name, srtImg, id)

                        Log.d("AAA" ,saveBrand.toString())
                        viewModel.upsertBrandAndSave(saveBrand) { check ->
                            if (check) {
                                viewModel.isSuccess.value = true
                            }
                        }

                    }
                } else {

                    val saveBrand = Thuonghieu(name, img, id)

                    viewModel.upsertBrandAndSave(saveBrand) { check ->
                        if (check) {
                            viewModel.isSuccess.value = true
                        }
                    }
                }
            }
        }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Thương hiệu", fontSize = 15.sp, fontWeight = FontWeight.Bold)

            OutlinedTextField(
                value = name,
                onValueChange = {
                    if (viewModel.nameErr) viewModel.nameErr = false
                    name = it
                    viewModel.name.value = it
                },
                label = { Text("Điền tên thương hiệu") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5,
                isError = viewModel.nameErr
            )

            // Selected images
            selectedImage?.let { imageUri ->
                AsyncImage(
                    model = imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } ?: Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .padding(4.dp)
            ) {
                AsyncImage(
                    model = if (img.isEmpty() || img.isBlank()) {
                        "https://img.upanh.tv/2024/11/07/34a81dd8afe55dfab95be9bf8bd701da.jpg"
                    } else {
                        viewModel.img.value
                    },
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                )
            }


            // Image picker
            Button(
                onClick = { pickImageLauncher.launch("image/*") },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Chọn ảnh")
            }

            if (viewModel.messageError.isNotEmpty()) {
                Text(
                    text = viewModel.messageError,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }


            // Submit button
            Button(
                onClick = {
                    handleReviewClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                enabled = !viewModel.isUploading
            ) {
                if (viewModel.isUploading) {
                    androidx.compose.material.CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(0.dp)
                    )
                } else {
                    Text("Lưu thương hiệu", fontWeight = FontWeight.Bold)
                }
            }
        }

        SuccessDialog(
            openDialog = viewModel.isSuccess,
            "Success !!",
            "Lưu thương hiệu thành công",
            "Xác nhận"
        ) {
            viewModel.isSuccess.value = false

            //load lai status
            coroutineScope.launch {
                viewModel.getBrands()
            }

            navController.popBackStack()
            mainViewModel.updateSelectedScreen(navController)
        }
    }
}