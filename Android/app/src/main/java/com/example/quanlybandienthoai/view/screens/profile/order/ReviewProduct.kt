package com.example.quanlybandienthoai.view.screens.profile.order

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.model.remote.entity.Review
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.screens.home.fontFamily
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.viewmodel.OrderViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductReviewScreen(
    navController: NavHostController,
    orderId: Int,
    idSP: Int,
    viewModel: OrderViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    LaunchedEffect(orderId, idSP) {
        viewModel.getReviewExist(orderId, idSP)
    }

    val review by viewModel.review.collectAsState()

    var rating by remember { mutableFloatStateOf(viewModel.rate) }
    var reviewText by remember { mutableStateOf(viewModel.reviewContent) }
    var img by remember { mutableStateOf(viewModel.img) }

    var isSuccess by remember { mutableStateOf(false) }

    var selectedImage by remember { mutableStateOf(viewModel.selectedImageUri) }
    val context = LocalContext.current

    LaunchedEffect(review) {
        if (review.content.isNotEmpty()) {
            rating = review.rate.toFloat()
            reviewText = review.content
            img = review.img
        }
    }
    if (review.content.isEmpty()) {
        rating = 0f
        reviewText = ""
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
                text = "🛍️ Bình luận sản phẩm " + viewModel.tensp, fontSize = 24.sp,
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
    }) { padding ->

        fun handleReviewClick() {

            if (viewModel.validateReviewProduct()) {
                viewModel.messageError = ""

                if (selectedImage != null) {
                    viewModel.uploadImage(
                        context, selectedImage!!,
                        nameImage = generateRandomCode()
                    ) { srtImg ->
                        viewModel.img = srtImg


                        val reviewUpsert = Review(
                            viewModel.idExist,
                            reviewText,
                            rating.toDouble(),
                            srtImg,
                            appViewModel.getCurrentUserId().toInt(),
                            idSP,
                            review.feeback,
                            if (review.feeback_content.isNullOrBlank()) null else review.feeback_content,
                            if (review.nhanvien.isNullOrBlank()) null else review.nhanvien,
                            if (review.ngayphanhoi.isNullOrBlank()) null else review.ngayphanhoi,
                            orderId,
                            LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                            appViewModel.getCurrentUser()!!.lastName + " " + appViewModel.getCurrentUser()!!.firstName
                        )

                        Log.d("AAA", reviewUpsert.toString())

                        //them danh gia
                        if (viewModel.idExist == -1) {
                            coroutineScope.launch {
                                val result = async {
                                    val saveReviewed = viewModel.reviewProduct(reviewUpsert)

                                    if (saveReviewed != null) {
                                        val updatedReview = reviewUpsert.copy(id = saveReviewed.id)
                                        viewModel.insertReviewFireBase(updatedReview)

                                        viewModel.updateInfoReviewProduct(idSP, rating.toDouble())
                                        true
                                    } else false
                                }.await()

                                viewModel.isReviewSucess.value = result
                                Log.d("AAA", "Success? $isSuccess")
                            }
                        }
                        //sua danh gia
                        else {
                            viewModel.updateReviewProduct(reviewUpsert)

                            val updatedReview = reviewUpsert.copy(id = viewModel.idExist)
                            coroutineScope.launch {
                                viewModel.insertReviewFireBase(updatedReview)
                            }

                            viewModel.isReviewSucess.value = true
                        }
                    }
                } else {

                    val reviewUpsert = Review(
                        viewModel.idExist,
                        reviewText,
                        rating.toDouble(),
                        img,
                        appViewModel.getCurrentUserId().toInt(),
                        idSP,
                        review.feeback,
                        if (review.feeback_content.isNullOrBlank()) null else review.feeback_content,
                        if (review.nhanvien.isNullOrBlank()) null else review.nhanvien,
                        if (review.ngayphanhoi.isNullOrBlank()) null else review.ngayphanhoi,
                        orderId,
                        LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        appViewModel.getCurrentUser()!!.lastName + " " + appViewModel.getCurrentUser()!!.firstName
                    )

                    //sua danh gia
                    if (viewModel.idExist != -1) {
                        viewModel.updateReviewProduct(reviewUpsert)

                        val updatedReview = reviewUpsert.copy(id = viewModel.idExist)
                        coroutineScope.launch {
                            viewModel.insertReviewFireBase(updatedReview)
                        }

                        viewModel.isReviewSucess.value = true
                    }
                }
            }
        }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Đánh giá sản phẩm", fontSize = 15.sp, fontWeight = FontWeight.Bold)

            // Rating stars
            RatingBar(
                rating = rating,
                onRatingChanged = {
                    rating = it
                    viewModel.rate = it
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // Review text
            OutlinedTextField(
                value = reviewText,
                onValueChange = {
                    if (viewModel.reviewContentErr) viewModel.reviewContentErr = false
                    reviewText = it
                    viewModel.reviewContent = it
                },
                label = { Text("Nhận xét về sản phẩm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(12.dp),
                maxLines = 5,
                isError = viewModel.reviewContentErr
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
            } ?: AsyncImage(
                model = if (img.isEmpty() || img.isBlank()) {
                    "https://img.upanh.tv/2024/11/07/34a81dd8afe55dfab95be9bf8bd701da.jpg"
                } else {
                    viewModel.img
                },
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )


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
                    Text("Gửi đánh giá", fontWeight = FontWeight.Bold)
                }
            }
        }

        SuccessDialog(
            openDialog = viewModel.isReviewSucess,
            "Success !!",
            "Lưu bình luận thành công",
            "Xác nhận"
        ) {
            viewModel.isReviewSucess.value = false

            //load lai status
            coroutineScope.launch {
                viewModel.getReviewExist(orderId, idSP)
            }
            navController.popBackStack()
            mainViewModel.updateSelectedScreen(navController)
        }
    }
}

@Composable
fun RatingBar(
    rating: Float,
    onRatingChanged: (Float) -> Unit,
    modifier: Modifier = Modifier,
    starCount: Int = 5
) {
    val starSize = 40.dp
    Row(modifier = modifier) {
        for (i in 1..starCount) {
            val icon = when {
                rating >= i -> Icons.Default.Star
                rating >= i - 0.5f -> Icons.Default.StarHalf
                else -> Icons.Default.StarBorder
            }

            Box(
                modifier = Modifier
                    .size(starSize)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val selectedRating = if (offset.x < size.width / 2) {
                                i - 0.5f
                            } else {
                                i.toFloat()
                            }
                            onRatingChanged(selectedRating)
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(starSize)
                )
            }
        }
    }
}


fun generateRandomCode(length: Int = 16): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}

