package com.example.quanlybandienthoai.view.screens.profile.address

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.model.remote.entity.address
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.CustomTextField
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AddreesViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertAddress(
    navController: NavController,
    context: Application = LocalContext.current.applicationContext as Application,
    viewModel: AddreesViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),

    ) {

    LaunchedEffect(Unit) {
        viewModel.reset()
        if (!viewModel.isSelected.value) {

            mainViewModel.updateSelectedScreen(navController)
        }
    }
    val coroutineScope = rememberCoroutineScope()

    val cityList = listOf(
        "Hà Nội", "Huế", "Lai Châu", "Điện Biên", "Sơn La", "Lạng Sơn",
        "Quảng Ninh", "Thanh Hoá", "Nghệ An", "Hà Tĩnh", "Cao Bằng",
        "Tuyên Quang", "Lào Cai", "Thái Nguyên", "Phú Thọ", "Bắc Ninh",
        "Hưng Yên", "Hải Phòng", "Ninh Bình", "Quảng Trị", "Đà Nẵng",
        "Quảng Ngãi", "Gia Lai", "Khánh Hòa", "Lâm Đồng", "Đắk Lắk",
        "Hồ Chí Minh", "Đồng Nai", "Tây Ninh", "Cần Thơ", "Vĩnh Long",
        "Đồng Tháp", "Cà Mau", "An Giang"
    )

    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Insert Address")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFF7D32A8),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            if (!viewModel.isSelected.value) {
                BottomNavigationBar(navController, mainViewModel)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            suspend fun handleAdd() {
                if (viewModel.validateInput()) {
                    viewModel.errorMessage = ""

                    val address = address(
                        -1,
                        viewModel.idkh.value,
                        viewModel.hovaten.value,
                        viewModel.email.value,
                        viewModel.sodienthoai.value,
                        viewModel.street_name.value,
                        viewModel.district.value,
                        viewModel.city.value,
                        "Việt Nam",
                        viewModel.note.value
                    )

                    viewModel.addNewAddress(address)
                    viewModel.reset()


                } else {
                    Toast.makeText(
                        context, viewModel.errorMessage, Toast.LENGTH_SHORT
                    ).show()
                }
            }

            CustomTextField(
                label = "Họ và tên",
                value = viewModel.hovaten.value,
                onValueChange = { viewModel.hovaten.value = it },
                isError = viewModel.nameError,
                icon = Icons.Default.Person
            )

            // Email
            CustomTextField(
                label = "Email",
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                isError = viewModel.emailErr,
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            // Số điện thoại
            CustomTextField(
                label = "Số điện thoại",
                value = viewModel.sodienthoai.value,
                onValueChange = { viewModel.sodienthoai.value = it },
                isError = viewModel.sdtErr,
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            // Street
            CustomTextField(
                label = "Tên đường",
                value = viewModel.street_name.value,
                onValueChange = { viewModel.street_name.value = it },
                isError = viewModel.streetErr,
                icon = Icons.Default.LocationOn
            )

            // Quận/Huyện
            CustomTextField(
                label = "Quận/Huyện",
                value = viewModel.district.value,
                onValueChange = { viewModel.district.value = it },
                isError = viewModel.districtErr,
                icon = Icons.Default.Map
            )

            // Thành phố - Dropdown
            ExposedDropdownMenuBox(
                modifier = Modifier.padding(vertical = 6.dp),
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    readOnly = true,
                    value = viewModel.city.value,
                    onValueChange = {},
                    label = { Text("Tỉnh/Thành phố") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    isError = viewModel.ctErr
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    cityList.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                viewModel.city.value = city
                                expanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = viewModel.note.value,
                onValueChange = {
                    viewModel.note.value = it
                },
                label = { Text("Ghi chú") },
                leadingIcon = { Icon(Icons.Default.Note, contentDescription = null) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = false,
                maxLines = 5,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .height(120.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF7D32A8),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Blue
                )
            )


            if (viewModel.errorMessage.isNotEmpty()) {
                Text(
                    text = viewModel.errorMessage,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp,
                    color = Color.Red
                )
            }


            Button(
                onClick = {
                    coroutineScope.launch {
                        handleAdd()
                    }
                },
                enabled = !viewModel.isSuccess.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = Color(android.graphics.Color.parseColor("#7d32a8"))
                ),
                shape = RoundedCornerShape(50)
            ) {

                Text(
                    text = "Thêm địa chỉ",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }

            SuccessDialog(
                openDialog = viewModel.isSuccess, "Success !!", "Add Address Complete", "Confirm"
            ) {
                viewModel.isSuccess.value = false
                navController.navigate(Screen.Account.Address.route)
            }
        }
    }

}