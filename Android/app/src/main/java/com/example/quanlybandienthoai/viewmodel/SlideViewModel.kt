package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.entity.Slide
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.repository.BrandRepository
import com.example.quanlybandienthoai.model.repository.SlideRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SlideViewModel (val context: Application) : AndroidViewModel(context) {
    private val slideRepository = SlideRepository


    var slides by mutableStateOf<List<Slide>>(emptyList())
        private set
    var isLoadingSlide by mutableStateOf(false)


    fun insertSlide(slide: Slide) {
        runBlocking {
            this.launch(Dispatchers.IO) {
                slideRepository.insertSlide(slide)
            }
        }
    }

    fun getAllSlides() {
        isLoadingSlide = true
        viewModelScope.launch {
            slides = slideRepository.getAllSlides()
        }
        isLoadingSlide = false
    }

}