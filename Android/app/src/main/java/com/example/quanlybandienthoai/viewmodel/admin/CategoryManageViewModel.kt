package com.example.quanlybandienthoai.viewmodel.admin

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnotegiuaki.upload.UploadRepository
import com.example.quanlybandienthoai.SearchBarState
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.BrandRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryManageViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo
    private val brandRepository = BrandRepository
    private val uploadRepository = UploadRepository()

    private val _brands = MutableStateFlow<List<Thuonghieu>>(emptyList())
    val brands: StateFlow<List<Thuonghieu>> = _brands
    private var allBrand = MutableStateFlow<List<Thuonghieu>>(emptyList())

    private val _brand = MutableStateFlow(Thuonghieu())
    val brand: StateFlow<Thuonghieu> = _brand

    var isSuccess = mutableStateOf(false)

    var isUploading by mutableStateOf(false)

    var name = mutableStateOf("")

    var img = mutableStateOf("")

    var selectedImageUri by mutableStateOf<Uri?>(null)


    var nameErr by mutableStateOf(false)
    var imgErr by mutableStateOf(false)
    var isError by mutableStateOf(false)


    var messageError by mutableStateOf("")

    fun searchByName(name: String) {
        val filterBrand = allBrand.value.filter { sp ->
            sp.tenthuonghieu.contains(name, ignoreCase = true)
        }

        _brands.value = filterBrand
    }

    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            val getBrand = quanlydienthoaiApi.getAllThuongHieus()
            allBrand.value = getBrand
            _brands.value = getBrand
        }
    }

    fun setCurrentBrand(brandId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _brand.value = Thuonghieu()
            val tempBrand = if (_brands.value.any { it.mathuonghieu == brandId }) {
                _brands.value.filter { it.mathuonghieu == brandId }[0]
            } else {
                Thuonghieu()
            }
            _brand.value = tempBrand

            name.value = _brand.value.tenthuonghieu
            img.value = brand.value.image
        }
    }

    fun upsertBrandAndSave(thuonghieu: Thuonghieu, onSave: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val saveBrand = quanlydienthoaiApi.upsertCategory(thuonghieu)
                if (saveBrand != null) {
                    brandRepository.insertCategory(saveBrand)
                    onSave(true)
                } else {
                    onSave(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun uploadImage(context: Context, uri: Uri, nameImage: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            isUploading = true // Bắt đầu upload
            val result = uploadRepository.uploadImage(context, uri, nameImage)
            isUploading = false // Kết thúc upload
            if (result != null) {
                onSuccess(result) // Gọi callback khi upload xong
            }
        }
    }

    fun deleteImage(publicId: String) {
        viewModelScope.launch {
            uploadRepository.deleteImage(publicId)
        }
    }

    fun validateFormCategory(): Boolean {
        if (name.value.isEmpty()) {
            nameErr = true
            messageError = "Chưa điền tên thương hiệu"
        }
        if (selectedImageUri == null) {
            imgErr = true
            messageError = "Chưa chọn ảnh đánh giá"
        }

        isError = nameErr || imgErr

        return isError
    }

    private val _searchBarState: MutableState<SearchBarState> =
        mutableStateOf(value = SearchBarState.CLOSED)
    val searchBarState: State<SearchBarState> = _searchBarState

    private val _searchTextState: MutableState<String> = mutableStateOf(value = "")
    val searchTextState: State<String> = _searchTextState

    fun updateSearchBarState(newValue: SearchBarState) {
        _searchBarState.value = newValue
    }

    fun updateSearchTextState(newValue: String) {
        _searchTextState.value = newValue
    }

}