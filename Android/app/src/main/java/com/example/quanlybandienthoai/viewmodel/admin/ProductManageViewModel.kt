package com.example.quanlybandienthoai.viewmodel.admin

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.appnotegiuaki.upload.UploadRepository
import com.example.quanlybandienthoai.SearchBarState
import com.example.quanlybandienthoai.model.remote.entity.ImportProduct
import com.example.quanlybandienthoai.model.remote.entity.RequestProductInteraction
import com.example.quanlybandienthoai.model.remote.entity.Sanpham
import com.example.quanlybandienthoai.model.remote.entity.Supplier
import com.example.quanlybandienthoai.model.remote.entity.Thuonghieu
import com.example.quanlybandienthoai.model.remote.entity.color
import com.example.quanlybandienthoai.model.remote.entity.hedieuhanh
import com.example.quanlybandienthoai.model.remote.entity.khuvuckho
import com.example.quanlybandienthoai.model.remote.entity.ram
import com.example.quanlybandienthoai.model.remote.entity.rom
import com.example.quanlybandienthoai.model.remote.entity.xuatxu
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.BrandRepository
import com.example.quanlybandienthoai.model.repository.ProductRepository
import com.example.quanlybandienthoai.view.screens.admin.sanpham.ProductVariant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale.Category

class ProductManageViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo
    private val productRepository = ProductRepository
    private val brandRepository = BrandRepository
    private val uploadRepository = UploadRepository()


    private val _product = MutableStateFlow(Sanpham())
    val product: StateFlow<Sanpham> = _product


    private val _products = MutableStateFlow<List<Sanpham>>(emptyList())
    val products: StateFlow<List<Sanpham>> = _products
    private var allProducts = MutableStateFlow<List<Sanpham>>(emptyList())

    private val _suppliers = MutableStateFlow<List<Supplier>>(emptyList())
    val suppliers: StateFlow<List<Supplier>> = _suppliers

    private val _ram = MutableStateFlow<List<ram>>(emptyList())
    val ram: StateFlow<List<ram>> = _ram
    private val _rom = MutableStateFlow<List<rom>>(emptyList())
    val rom: StateFlow<List<rom>> = _rom
    private val _color = MutableStateFlow<List<color>>(emptyList())
    val color: StateFlow<List<color>> = _color
    private val _xx = MutableStateFlow<List<xuatxu>>(emptyList())
    val xx: StateFlow<List<xuatxu>> = _xx
    private val _hdh = MutableStateFlow<List<hedieuhanh>>(emptyList())
    val hdh: StateFlow<List<hedieuhanh>> = _hdh
    private val _kho = MutableStateFlow<List<khuvuckho>>(emptyList())
    val kho: StateFlow<List<khuvuckho>> = _kho
    private val _brands = MutableStateFlow<List<Thuonghieu>>(emptyList())
    val brands: StateFlow<List<Thuonghieu>> = _brands

    private val _brand = MutableStateFlow<Thuonghieu?>(null)
    val brand: StateFlow<Thuonghieu?> = _brand

    var isConfirm = mutableStateOf(false)

    var isExit = mutableStateOf(false)

    var isImport = mutableStateOf(false)

    var isUpsert = mutableStateOf(false)


    var img = mutableStateOf("")


    var productName = MutableStateFlow("")
    var mainImageUri = MutableStateFlow<Uri?>(null)
    var subImageUris = MutableStateFlow<List<Uri>>(emptyList())
    var selectedXX = mutableStateOf("")
    var selectedHDH = mutableStateOf("")
    var selectedTH = mutableStateOf("")
    var giamgia = mutableStateOf("")
    var giare = mutableStateOf("")
    var dungluongpin = mutableStateOf("")
    var cameratruoc = mutableStateOf("")
    var camerasau = mutableStateOf("")
    var phienbanHDH = mutableStateOf("")
    var manhinh = mutableStateOf("")
    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var selectedWarehouse = mutableStateOf("")
    var isTragop = mutableStateOf(false)


    var selectedXXID = mutableStateOf(-1)
    var selectedHDHID = mutableStateOf(-1)
    var selectedTHID = mutableStateOf(-1)
    var selectedWarehouseID = mutableStateOf(-1)

    var productNameErr by mutableStateOf(false)
    var mainImageUriErr by mutableStateOf(false)
    var selectedXXErr by mutableStateOf(false)
    var selectedHDHErr by mutableStateOf(false)
    var selectedTHErr by mutableStateOf(false)
    var dungluongpinErr by mutableStateOf(false)
    var cameratruocErr by mutableStateOf(false)
    var camerasauErr by mutableStateOf(false)
    var phienbanHDHErr by mutableStateOf(false)
    var manhinhErr by mutableStateOf(false)
    var titleErr by mutableStateOf(false)
    var selectedWarehouseErr by mutableStateOf(false)
    var giamgiaErr by mutableStateOf(false)
    var giareErr by mutableStateOf(false)

    var err by mutableStateOf(false)

    var errorMessage by mutableStateOf("")

    fun initData() {
        viewModelScope.launch(Dispatchers.IO) {
            _ram.value = quanlydienthoaiApi.getRams()!!
            _rom.value = quanlydienthoaiApi.getRoms()!!
            _color.value = quanlydienthoaiApi.getColors()!!
            _xx.value = quanlydienthoaiApi.getXX()!!
            _kho.value = quanlydienthoaiApi.getKho()!!
            _brands.value = quanlydienthoaiApi.getAllThuongHieus()
            _hdh.value = quanlydienthoaiApi.getHDH()!!
        }
    }

    var isSelectImg = mutableStateOf(false)


    fun initProductEdit() {
        productName.value = _product.value.tensp
        val imageUrls = _product.value.hinhanh.split(",")
        mainImageUri.value = imageUrls.getOrNull(0)?.toUri()
        subImageUris.value = imageUrls.drop(1).map { it.toUri() }
        img.value = _product.value.hinhanh
        selectedXX.value = _product.value.xuatxu
        selectedHDH.value = _product.value.hedieuhanh.toString()
        selectedTH.value = _brand.value?.tenthuonghieu ?: ""
        selectedWarehouse.value = _product.value.khuvuckho
        dungluongpin.value = _product.value.dungluongpin.toInt().toString()
        cameratruoc.value = _product.value.cameratruoc.toString()
        camerasau.value = _product.value.camerasau.toString()
        phienbanHDH.value = _product.value.phienbanhdh?.toInt().toString()
        manhinh.value = _product.value.manhinh
        title.value = _product.value.sortDesc.toString()
        description.value = _product.value.detail.toString()

        _product.value.promo.forEach { map ->
            when (map["name"]) {
                "giamgia" -> giamgia.value = map["value"].toString()
                "giareonline" -> giare.value = map["value"].toString()
                "tragop" -> isTragop.value = true
            }
        }

    }


    fun insertProduct(requestProductInteraction: RequestProductInteraction) {
        isUploading = true
        viewModelScope.launch(Dispatchers.IO) {
//            delay(3000)
            quanlydienthoaiApi.insertProduct(requestProductInteraction)
        }
        isUploading = false
    }

    fun updateProduct(requestProductInteraction: RequestProductInteraction) {
        isUploading = true
        viewModelScope.launch(Dispatchers.IO) {
//            delay(3000)
            quanlydienthoaiApi.updateProduct(requestProductInteraction)
        }
        isUploading = false
    }

    fun validateInput(): Boolean {
        if (productName.value.isEmpty()) {
            productNameErr = true
            errorMessage = "Vui lòng điền tên sản phẩm"
        } else if (mainImageUri.value == null) {
            mainImageUriErr = true
            errorMessage = "Vui lòng chọn ảnh chính cho sản phẩm"
        } else if (giamgia.value.isEmpty()) {
            giamgiaErr = true
            errorMessage = "Vui lòng điền giảm giá"
        } else if (giare.value.isEmpty()) {
            giareErr = true
            errorMessage = "Vui lòng điền khuyến mãi"
        }
        else if (selectedXX.value.isEmpty()) {
            selectedXXErr = true
            errorMessage = "Vui lòng chọn xuất xứ"
        } else if (selectedHDH.value.isEmpty()) {
            selectedHDHErr = true
            errorMessage = "Vui lòng chọn hệ điều hành"
        } else if (selectedTH.value.isEmpty()) {
            selectedTHErr = true
            errorMessage = "Vui lòng chọn thương hiệu"
        } else if (dungluongpin.value.isEmpty()) {
            dungluongpinErr = true
            errorMessage = "Vui lòng điền dung lương pin"
        } else if (camerasau.value.isEmpty()) {
            camerasauErr = true
            errorMessage = "Vui lòng điền camera sau"
        } else if (cameratruoc.value.isEmpty()) {
            cameratruocErr = true
            errorMessage = "Vui lòng điền camera trước"
        } else if (manhinh.value.isEmpty()) {
            manhinhErr = true
            errorMessage = "Vui lòng điền cấu hình màn hình"
        } else if (phienbanHDH.value.isEmpty()) {
            phienbanHDHErr = true
            errorMessage = "Vui lòng điền phiên bản hệ điều hành"
        } else if (selectedWarehouse.value.isEmpty()) {
            selectedWarehouseErr = true
            errorMessage = "Vui lòng chọn khu vực kho chứa"
        } else if (title.value.isEmpty()) {
            titleErr = true
            errorMessage = "Vui lòng điền mô tả ngắn"
        }

        err =
            productNameErr || mainImageUriErr|| giareErr || giamgiaErr || selectedXXErr || selectedHDHErr || selectedTHErr || dungluongpinErr
                    || cameratruocErr || camerasauErr || phienbanHDHErr || manhinhErr || titleErr || selectedWarehouseErr
        return !err
    }

    var isUploading by mutableStateOf(false)

//    suspend fun uploadImageSuspend(context: Context, uri: Uri, nameImage: String): String? {
//        return withContext(Dispatchers.IO) {
//            uploadRepository.uploadImage(context, uri, nameImage)
//        }
//    }

    suspend fun uploadImageSuspend(context: Context, uri: Uri, nameImage: String): String? {
        return if (uri.toString().startsWith("http")) {
            // Đã là URL Cloudinary, không cần upload lại
            uri.toString()
        } else {
            deleteImage(nameImage)

            withContext(Dispatchers.IO) {
                uploadRepository.uploadImage(context, uri, nameImage)
            }
        }
    }

    fun deleteImage(publicId: String) {
        viewModelScope.launch {
            uploadRepository.deleteImage(publicId)
        }
    }

    fun reset() {
        subImageUris.value = emptyList()
        productName.value = ""
        mainImageUri.value = null
        selectedXX.value = ""
        selectedHDH.value = ""
        selectedTH.value = ""
        giamgia.value = ""
        giare.value = ""
        dungluongpin.value = ""
        cameratruoc.value = ""
        camerasau.value = ""
        title.value = ""
        manhinh.value = ""
        description.value = ""
        selectedWarehouse.value = ""
        errorMessage = ""
        phienbanHDH.value = ""
        selectedXXID.value = -1
        selectedHDHID.value = -1
        selectedTHID.value = -1
        selectedWarehouseID.value = -1
        img.value = ""
        isTragop.value = false

        err = false
        productNameErr = false
        mainImageUriErr = false
        selectedXXErr = false
        selectedHDHErr = false
        selectedTHErr = false
        dungluongpinErr = false
        cameratruocErr = false
        camerasauErr = false
        phienbanHDHErr = false
        manhinhErr = false
        titleErr = false
        selectedWarehouseErr = false
        isUploading = false
        isSelectImg.value=false
    }

    fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val getProduct = quanlydienthoaiApi.getProducts()
            _products.value = getProduct
            allProducts.value = getProduct
        }
    }

    fun searchByName(name: String) {
        val filterProduct = allProducts.value.filter { sp ->
            sp.tensp.contains(name, ignoreCase = true)
        }

        _products.value = filterProduct
    }

    fun getSuppliers() {
        viewModelScope.launch(Dispatchers.IO) {
            _suppliers.value = quanlydienthoaiApi.getSuppliers()!!
        }
    }

    fun setCurrentProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.value = Sanpham()
            val tempProduct = _products.value.filter { it.masp == productId }[0]
            _product.value = tempProduct // Cập nhật sản phẩm trực tiếp vào StateFlow
            _brand.value = brandRepository.getCategory(tempProduct.thuonghieu)
        }
    }

    fun deleteProduct(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            quanlydienthoaiApi.deleteProduct(productId)
            productRepository.deleteProduct(productId.toString())
            _products.value = _products.value.filterNot { it.masp == productId }
            allProducts.value = _products.value.filterNot { it.masp == productId }
        }
    }

    fun importProduct(importProduct: ImportProduct, masp: Int, soluongthem: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            quanlydienthoaiApi.importProduct(importProduct)
//            productRepository.updateNumberProduct(masp, soluongthem)
        }
    }

    fun importProductUpdateInFireBase(soLuongNhapTheoPhienBan: Map<Int, Int>){
        viewModelScope.launch(Dispatchers.IO) {
            productRepository.updateSoLuongImportTuMapPhienBan(soLuongNhapTheoPhienBan)
        }
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