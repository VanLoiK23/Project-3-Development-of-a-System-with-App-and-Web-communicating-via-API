package com.example.quanlybandienthoai.view.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.model.remote.entity.Slide
import kotlinx.coroutines.delay

@Composable
fun BannerSlider(
    slides: List<Slide>
) {

    val newSlides = listOf(
        Slide(image = "https://res.cloudinary.com/deynh1vvv/image/upload/v1753843699/b7c7d55a5d2fe0c2b78e29e11064d7c2_xz9hvc.jpg"),
        Slide(image = "https://i.ibb.co/DgsxHP4Q/a838bf7d72a1a6f111de79b45c5c9718.jpg"),
        Slide(image = "https://i.ibb.co/WvXG8t5v/167a10280060e5a3d289cf5660bedb87.jpg"),
        Slide(image = "https://i.ibb.co/hrsT7ZN/2af101d413efd31647e1ed69e4db2619.jpg")
    ) + slides

    val listState = rememberLazyListState()
    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(currentIndex, newSlides) {
        if (newSlides.isNotEmpty()) {
            while (true) {
                delay(3000) // Đổi ảnh sau mỗi 3 giây
                val nextIndex = (currentIndex + 1) % newSlides.size
                listState.animateScrollToItem(nextIndex)
                currentIndex = nextIndex
            }
        }
    }


    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
//        item {
//            BannerItem(Slide(image = "https://res.cloudinary.com/deynh1vvv/image/upload/v1753843699/b7c7d55a5d2fe0c2b78e29e11064d7c2_xz9hvc.jpg"))
//            BannerItem(Slide(image = "https://i.ibb.co/DgsxHP4Q/a838bf7d72a1a6f111de79b45c5c9718.jpg"))
//        }
        items(newSlides) { slide ->
            BannerItem(slide)
        }
    }
}

@Composable
fun BannerItem(slide: Slide) {
    Box(
        modifier = Modifier
            .width(370.dp)
            .height(160.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.LightGray)
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = slide.image,
                imageLoader = LocalContext.current.let {
                    ImageLoader.Builder(it)
                        .components {
                            add(GifDecoder.Factory()) // Bật hỗ trợ GIF
                        }
                        .build()
                }
            ),
            contentDescription = "Banner Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

    }
}