package com.example.quanlybandienthoai.view.screens.product

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.quanlybandienthoai.model.remote.entity.Review
import kotlin.math.floor

@SuppressLint("DefaultLocale")
@Composable
fun ProductReviewsSection(reviews: List<Review>,totalReview:Int,totalRate:Double) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedRating by remember { mutableStateOf(0) }


    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = "Đánh giá sản phẩm",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        val avg = totalRate.toDouble() / totalReview
        val result = String.format("%.1f", avg)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = result,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFA500)
            )
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Rating Star",
                tint = Color(0xFFFFA500)
            )
            Text(
                text = "Trung bình dựa trên ${totalReview} đánh giá",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Button(onClick = { showDialog = true }, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Xem thêm đánh giá")
        }
        reviews.take(5).forEach { review ->
            ReviewItem(review)
        }
    }

    if (showDialog) {
        var selectedStar by remember { mutableStateOf(0) }

        AlertDialog(
            onDismissRequest = {
                showDialog = false
            },
            confirmButton = {},
            dismissButton = {
                IconButton(onClick = { showDialog = false }) {
                    Icon(Icons.Default.Close, contentDescription = "Đóng")
                }
            },
            title = { Text("Xem Đánh Giá", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    // Bộ lọc sao
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Lọc theo sao:", fontWeight = FontWeight.Bold)
                        DropdownMenuItem(selectedStar) { selectedStar = it }
                    }

                    // Danh sách đánh giá
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val filteredReviews = if (selectedStar > 0) {
                            reviews.filter { floor(it.rate).toInt() == selectedStar }
                        } else reviews

                        items(filteredReviews) { review ->
                            ReviewItem(review)
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            // Hiển thị user & ngày đánh giá
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "User Icon")
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = review.user, fontWeight = FontWeight.Bold)
                    Text(
                        text = review.ngay_đanhgia,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))

            // Hiển thị số sao đánh giá
            Row(verticalAlignment = Alignment.CenterVertically) {
                for (i in 1..5) {
                    val icon = when {
                        review.rate >= i -> Icons.Default.Star
                        review.rate >= i - 0.5 -> Icons.Default.StarHalf
                        else -> Icons.Default.StarBorder
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = "Rating Star",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            // Nội dung đánh giá
            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(6.dp))

            // Hiển thị hình ảnh nếu có
            if (review.img.isNotEmpty()) {
                AsyncImage(
                    model = review.img,
                    contentDescription = "Review Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Nếu có phản hồi từ nhân viên
            if (review.feeback == 1) {
                Divider(color = Color.Gray, thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Verified, contentDescription = "Verified Icon", tint = Color.Blue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(text = "Nhân viên: ${review.nhanvien}", fontWeight = FontWeight.Bold, color = Color.Blue)
                        Text(text = review.feeback_content!!, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "Ngày phản hồi: ${review.ngayphanhoi}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}



@Composable
fun DropdownMenuItem(selectedRating: Int, onRatingSelected: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val ratings = listOf(0, 1, 2, 3, 4, 5)

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = if (selectedRating == 0) "⭐" else "$selectedRating ⭐")
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            ratings.forEach { rating ->
                androidx.compose.material3.DropdownMenuItem(
                    onClick = {
                        onRatingSelected(rating)
                        expanded = false
                    },
                    text = { Text(text = if (rating == 0) "Tất cả" else "$rating ⭐") }
                )
            }
        }
    }
}