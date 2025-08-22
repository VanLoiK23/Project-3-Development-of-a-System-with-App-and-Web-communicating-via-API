package com.example.quanlybandienthoai.view.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun TechnicalSpecsTable(specs: List<Pair<String, String?>>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            specs.forEachIndexed { index, spec ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(if (index % 2 == 0) Color(0xFFF8F9FA) else Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(120.dp)

                            .background(Color(0xFF007BFF)) // Màu xanh
                    ) {
                        Text(
                            text = spec.first,
                            color = Color.White, // Chữ trắng
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    spec.second?.let {
                        Text(
                            text = it,
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,  // Chỉ cho phép một dòng
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}