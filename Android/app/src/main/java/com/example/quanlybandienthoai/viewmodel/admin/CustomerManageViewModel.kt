package com.example.quanlybandienthoai.viewmodel.admin

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quanlybandienthoai.SearchBarState
import com.example.quanlybandienthoai.model.remote.entity.User
import com.example.quanlybandienthoai.model.remote.repo.QuanlydienthoaiRepo
import com.example.quanlybandienthoai.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CustomerManageViewModel(val context: Application) : AndroidViewModel(context) {
    private val quanlydienthoaiApi = QuanlydienthoaiRepo
    private val userRepository = UserRepository

    private val _customers = MutableStateFlow<List<User>>(emptyList())
    val customers: StateFlow<List<User>> = _customers
    private val allUser = MutableStateFlow<List<User>>(emptyList())

    var customer by mutableStateOf<User?>(null)

    var isSuccess = mutableStateOf(false)

    var isComfirm = mutableStateOf(false)
    var isComfirmDelete = mutableStateOf(false)


    var isEdit = mutableStateOf(false)


    fun getCustomers() {
        viewModelScope.launch(Dispatchers.IO) {
            val getUsers = quanlydienthoaiApi.getAllUser()
            _customers.value = getUsers

            _customers.update { list ->
                list.filterNot { it.role.contains("nhân viên")}
            }
            allUser.value = getUsers
            allUser.update { list ->
                list.filterNot { it.role.contains("nhân viên")}
            }
        }
    }


    fun searchByName(name: String) {
        val searchTerm = name.trim()
        val filterCustomer = allUser.value.filter { sp ->
            "${sp.lastName} ${sp.firstName}".contains(searchTerm, ignoreCase = true)
        }
        _customers.value = filterCustomer
    }


    fun setCurrentCustomer(customerId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            customer = null
            val tempOrder = _customers.value.filter { it.id == customerId }[0]
            customer = tempOrder
        }
    }

    fun updateCustomerStatus(customerId: Int, status: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                quanlydienthoaiApi.updateStatusCustomer(customerId, status)

                val isUpdated = userRepository.updateUserStatusById(customerId, status)
                if (!isUpdated) throw Exception("Cập nhật thất bại trong Firestore")

                _customers.update { list ->
                    list.map { customerMap ->
                        if (customerMap.id == customerId) customerMap.copy(status = status) else customerMap
                    }
                }

                if (status == "deleted") {
                    val isDeleted = userRepository.deleteUserById(customerId)
                    if (!isDeleted) throw Exception("Xóa thất bại trong Firestore")

                    _customers.update { list ->
                        list.filterNot { it.id == customerId }
                    }

                    allUser.update { list ->
                        list.filterNot { it.id == customerId }
                    }
                }

                isSuccess.value = true

            } catch (e: Exception) {
                println("Lỗi khi cập nhật trạng thái khách hàng: ${e.message}")
                isSuccess.value = false
            }
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