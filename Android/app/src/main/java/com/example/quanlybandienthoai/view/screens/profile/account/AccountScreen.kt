package com.example.quanlybandienthoai.view.screens.profile.account

import android.annotation.SuppressLint
import android.app.Application
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FireTruck
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.quanlybandienthoai.view.components.BottomNavigationBar
import com.example.quanlybandienthoai.view.components.CustomTextField
import com.example.quanlybandienthoai.view.components.DatePickerDialog
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AccountViewModel
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import com.example.quanlybandienthoai.worker.SyncFirebaseToApiWorker
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    viewModel: AccountViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity),
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
) {

    LaunchedEffect(Unit) {
//        viewModel.reset()

        viewModel.isAdmin = false
        mainViewModel.updateSelectedScreen(navController)

        viewModel.userId = appViewModel.getCurrentUserId()

        if (viewModel.dbUser == null) {
            viewModel.initUser()
        }

    }
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current


    val userId = appViewModel.getCurrentUserId()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Manage Account")
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

            BottomNavigationBar(navController, mainViewModel)

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            suspend fun handleAdd() {
                if (viewModel.validateManageFormInput()) {
                    viewModel.errorMessage = ""

                    viewModel.updateUser()
//                    viewModel.reset()
                    viewModel.openDialog.value = true


                    //update account in mysql
                    val inputData = workDataOf("userId" to userId)

                    val workRequest = OneTimeWorkRequestBuilder<SyncFirebaseToApiWorker>()
                        .setInputData(inputData)
                        .build()

                    WorkManager.getInstance(context).enqueue(workRequest)
                } else {
                    Toast.makeText(
                        context, viewModel.errorMessage, Toast.LENGTH_SHORT
                    ).show()
                }
            }

            CustomTextField(
                label = "Họ và tên",
                value = viewModel.username,
                onValueChange = { viewModel.username = it },
                isError = viewModel.uNameError,
                icon = Icons.Default.Person
            )

            // Email
            CustomTextField(
                label = "Email",
                value = viewModel.email,
                onValueChange = { viewModel.email = it },
                isError = viewModel.emailError,
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            // Số điện thoại
            CustomTextField(
                label = "Số điện thoại",
                value = viewModel.phone,
                onValueChange = { viewModel.phone = it },
                isError = viewModel.phoneError,
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            val calendar = Calendar.getInstance()

            val birthDateString = viewModel.birthDate.value
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthDate = try {
                sdf.parse(birthDateString)
            } catch (e: Exception) {
                null
            }

            birthDate?.let {
                calendar.time = it // Đặt ngày sinh của người dùng vào calendar
            } ?: run {
                // Nếu không có ngày sinh trong viewModel, sử dụng ngày hiện tại
                calendar.time = Date()
            }


            val dateDialog = android.app.DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    // Cập nhật ngày sinh khi người dùng chọn
                    val selectedDate = "$dayOfMonth/${month + 1}/$year"
                    viewModel.birthDate.value = selectedDate
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            val displayDate = remember(viewModel.birthDate.value) {
                val birthDateString = viewModel.birthDate.value
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

                val date = try {
                    sdf.parse(birthDateString)
                } catch (e: Exception) {
                    null
                }

                date?.let {
                    sdf.format(it)
                } ?: ""
            }

            OutlinedTextField(
                value = displayDate,
                onValueChange = {
                    viewModel.birthDate.value = it
                },
                label = { Text("Birthdate: ") },
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                enabled = false,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable(indication = null,
                        interactionSource = remember { MutableInteractionSource() }) {
                        focusManager.clearFocus()
                        dateDialog.show()
                    },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(
                        android.graphics.Color.parseColor("#7d32a8")
                    ),
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Blue
                ),
                isError = viewModel.dobError
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
                    text = "Lưu thay đổi",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

            }

            SuccessDialog(
                openDialog = viewModel.openDialog,
                "Success !!",
                "Update Infomation Account Complete",
                "Confirm"
            ) {
                viewModel.openDialog.value = false
                viewModel.dbUser = null
//                navController.navigate(Screen.Account.Main.route)
            }
        }
    }

}





