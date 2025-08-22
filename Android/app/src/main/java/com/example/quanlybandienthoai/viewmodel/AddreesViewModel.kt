package com.example.quanlybandienthoai.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.model.remote.api.QuanlydienthoaiApi
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddreesViewModel(val context: Application) : AndroidViewModel(context) {
    val quanlydienthoaiApi = QuanlydienthoaiRepo

    var id_shipping = mutableStateOf(0)
        private set

    var idkh = mutableStateOf(0)
        private set
    var hovaten = mutableStateOf("")
        private set
    var nameError by mutableStateOf(false)

    var email = mutableStateOf("")
        private set
    var emailErr by mutableStateOf(false)

    var sodienthoai = mutableStateOf("")
        private set
    var sdtErr by mutableStateOf(false)

    var street_name = mutableStateOf("")
        private set
    var streetErr by mutableStateOf(false)

    var district = mutableStateOf("")
        private set
    var districtErr by mutableStateOf(false)

    var city = mutableStateOf("")
        private set
    var ctErr by mutableStateOf(false)

    var note = mutableStateOf("")
        private set

    var isSuccess = mutableStateOf(false)
        private set

    var isSelected = mutableStateOf(false)
        private set

    var addresss by mutableStateOf<List<address>>(emptyList())
        private set

    var address by mutableStateOf<address?>(null)
        private set

    var errorMessage by mutableStateOf("")

    var err by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
        private set

    var isUpsert by mutableStateOf(false)


    fun clear() {
        address = null
        addresss = emptyList()
        isLoading = false
        isUpsert = false
        errorMessage = ""
    }

    fun validateInput(): Boolean {
        if (hovaten.value.isEmpty()) {
            nameError = true
            errorMessage = "Please enter your name"
        } else if (email.value.isEmpty()) {
            emailErr = true
            errorMessage = "Please enter your email"
        } else if (sodienthoai.value.isEmpty()) {
            sdtErr = true
            errorMessage = "Please enter your phone"
        } else if (district.value.isEmpty()) {
            districtErr = true
            errorMessage = "Please enter your district"
        } else if (street_name.value.isEmpty()) {
            streetErr = true
            errorMessage = "Please enter your street"
        } else if (city.value.isEmpty()) {
            ctErr = true
            errorMessage = "Please enter your city or prohibit"
        }

        err = nameError || emailErr || sdtErr || districtErr || streetErr || ctErr
        return !err
    }


    fun getAllAddress() {
        isLoading = true
        viewModelScope.launch {
            addresss = quanlydienthoaiApi.getAddress(id = idkh.value)
        }
        isLoading = false
        isUpsert = false
    }

    fun getAddress(id: Int) {
        viewModelScope.launch {
            address = addresss.filterNotNull().firstOrNull { it.id == id }

            if (address != null) {
                hovaten.value = address!!.hovaten
                email.value = address!!.email
                street_name.value = address!!.street_name
                district.value = address!!.district
                sodienthoai.value = address!!.sodienthoai
                city.value = address!!.city
                note.value = address!!.note
                id_shipping.value = address!!.id
            }
        }
    }

    fun addNewAddress(address: address) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = quanlydienthoaiApi.addAddress(address)
            withContext(Dispatchers.Main) {
                isUpsert = true
                if (result != null) {
                    isSuccess.value = true
                } else {
                    Log.e("ADDRESS", "FAILED")
                }
                reset()
            }
        }
    }

    fun updateExistingAddress(id: Int, address: address) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = quanlydienthoaiApi.updateAddress(id, address)
            withContext(Dispatchers.Main) {
                isUpsert = true
                if (!result.isNullOrEmpty()) {
                    isSuccess.value = true
                }
                reset()
            }
        }
    }


    fun deleteAddress(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = quanlydienthoaiApi.deleteAddress(id)
            withContext(Dispatchers.Main) {
                isUpsert = true
                if (result) {
                    isSuccess.value = true
                }
            }
        }
    }


    fun reset() {
        hovaten.value = ""
        email.value = ""
        street_name.value = ""
        city.value = ""
        district.value = ""
        note.value = ""
        sodienthoai.value = ""
        errorMessage = ""
        err = false
        nameError = false
        emailErr = false
        sdtErr = false
        districtErr = false
        streetErr = false
        ctErr = false
    }


}