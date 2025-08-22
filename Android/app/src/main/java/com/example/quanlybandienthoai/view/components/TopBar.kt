package com.example.quanlybandienthoai.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.size.Scale
import com.example.quanlybandienthoai.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String = "",
    haveLogo: Boolean,
    navBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val loraFontFamily = FontFamily(
        Font(R.font.lora_regular, FontWeight.Normal),
        Font(R.font.lora_medium, FontWeight.Medium),
        Font(R.font.lora_mediumitalic, FontWeight.Medium),
        Font(R.font.lora_semibolditalic, FontWeight.SemiBold),
        Font(R.font.lora_bold, FontWeight.Bold),
        Font(R.font.lora_bolditalic, FontWeight.Bold),
        Font(R.font.lora_italic, FontWeight.Light)
    )

    TopAppBar(
        colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color.White),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (haveLogo) {
                    Image(
                        painter = painterResource(R.drawable.icon_logo),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(70.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Khoảng cách giữa icon và text
                }
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = loraFontFamily,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        navigationIcon = {
            navBack?.let {
                IconButton(onClick = { navBack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Go to previous screen."
                    )
                }
            }
        },
        actions = actions,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val borderSize = 2.dp.toPx() // Độ dày border
                drawLine(
                    color = Color.LightGray, // Màu border
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = borderSize
                )
            }
            .shadow(6.dp)

    )
}
