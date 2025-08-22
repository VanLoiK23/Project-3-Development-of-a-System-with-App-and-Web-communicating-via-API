package com.example.quanlybandienthoai.view

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.components.DatePickerDialog
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.viewmodel.ForgotPasswordViewModel
import com.example.quanlybandienthoai.viewmodel.RegisterViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel(LocalContext.current as ComponentActivity)
) {
    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff"))),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.top_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.graphicsLayer(
                rotationY = 180f // Xoay 180 độ
            )
        )

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(100.dp)
        )

        Text(
            text = "Register",
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor("#7d32a8"))
        )

        OutlinedTextField(
            value = viewModel.firstName,
            onValueChange = {
                if (viewModel.fNameError) viewModel.fNameError = false
                viewModel.firstName = it
            },
            label = { Text("First name: ") }, // Nhãn hiển thị trên TextField
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "User"
                )
            }, // Icon bên trái
            trailingIcon = {
                if (viewModel.firstName.isNotEmpty()) {
                    IconButton(onClick = { viewModel.firstName = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text")
                    }
                }
            }, // Nút xóa bên phải

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Bàn phím email
                imeAction = ImeAction.Next // Hành động "Done"
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) } // Ẩn bàn phím khi nhấn "Done"
            ),

            singleLine = true, // Chỉ nhập 1 dòng
            maxLines = 1, // Giới hạn dòng tối đa
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors( // Dùng nếu là `OutlinedTextField`
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Blue
            ),
            isError = viewModel.fNameError

        )

        OutlinedTextField(
            value = viewModel.lastName,
            onValueChange = {
                if (viewModel.lNameError) viewModel.lNameError = false
                viewModel.lastName = it
            },
            label = { Text("Last name: ") }, // Nhãn hiển thị trên TextField
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    Icons.Default.PersonOutline,
                    contentDescription = "User"
                )
            }, // Icon bên trái
            trailingIcon = {
                if (viewModel.lastName.isNotEmpty()) {
                    IconButton(onClick = { viewModel.lastName = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text")
                    }
                }
            }, // Nút xóa bên phải

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text, // Bàn phím email
                imeAction = ImeAction.Next // Hành động "Done"
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) } // Ẩn bàn phím khi nhấn "Done"
            ),

            singleLine = true, // Chỉ nhập 1 dòng
            maxLines = 1, // Giới hạn dòng tối đa
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors( // Dùng nếu là `OutlinedTextField`
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Blue
            ),
            isError = viewModel.lNameError
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                if (viewModel.emailError) viewModel.emailError = false
                viewModel.email = it
            },
            label = { Text("Email: ") }, // Nhãn hiển thị trên TextField
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    Icons.Default.Email,
                    contentDescription = "User"
                )
            }, // Icon bên trái
            trailingIcon = {
                if (viewModel.email.isNotEmpty()) {
                    IconButton(onClick = { viewModel.email = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text")
                    }
                }
            }, // Nút xóa bên phải

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email, // Bàn phím email
                imeAction = ImeAction.Next // Hành động "Done"
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) } // Ẩn bàn phím khi nhấn "Done"
            ),

            singleLine = true, // Chỉ nhập 1 dòng
            maxLines = 1, // Giới hạn dòng tối đa
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors( // Dùng nếu là `OutlinedTextField`
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Blue
            ),
            isError = viewModel.emailError
        )

        OutlinedTextField(
            value = viewModel.phone,
            onValueChange = {
                if (viewModel.phoneError) viewModel.phoneError = false
                viewModel.phone = it
            },
            label = { Text("Phone: ") }, // Nhãn hiển thị trên TextField
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "User"
                )
            }, // Icon bên trái
            trailingIcon = {
                if (viewModel.phone.isNotEmpty()) {
                    IconButton(onClick = { viewModel.phone = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear text")
                    }
                }
            }, // Nút xóa bên phải

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number, // Bàn phím email
                imeAction = ImeAction.Next // Hành động "Done"
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) } // Ẩn bàn phím khi nhấn "Done"
            ),

            singleLine = true, // Chỉ nhập 1 dòng
            maxLines = 1, // Giới hạn dòng tối đa
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors( // Dùng nếu là `OutlinedTextField`
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Blue
            ),
            isError = viewModel.phoneError
        )

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = {
                if (viewModel.passwordError) viewModel.passwordError = false
                viewModel.password = it
            },
            label = { Text("Password: ") },
            singleLine = true,
            visualTransformation = if (viewModel.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    Icons.Default.Password, contentDescription = "User"
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            trailingIcon = {
                val image =
                    if (viewModel.passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                IconButton(onClick = { viewModel.passwordVisible = !viewModel.passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ), // Màu viền khi focus (Lavender)
                unfocusedBorderColor = Color.Gray, // Màu viền khi không focus
                cursorColor = Color.Blue // Màu con trỏ
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            isError = viewModel.passwordError,
        )

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -18)

        val dateDialog = DatePickerDialog(
            viewModel.birthDate, context, calendar, calendar.timeInMillis
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
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(5.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        handleRegister(viewModel, context)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(66.dp)
                    .padding(start = 64.dp, end = 64.dp, top = 8.dp, bottom = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(
                        android.graphics.Color.parseColor(
                            "#7d32a8"
                        )
                    )
                ),
                shape = RoundedCornerShape(50),
                enabled = !viewModel.isLoading

            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                } else {
                    Text(
                        text = "Create an account",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            SuccessDialog(
                viewModel.openDialog,
                "Đăng ký thành công",
                "Đăng nhập và trải nghiệm cảm giác mua sắm thôi nào!",
                "Login"
            ) { navToLogin() }
        }

        Text(
            text = "Have a account ? Click here!",
            Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .clickable {
                    navToLogin()
                },
            fontSize = 14.sp,
            color = Color(android.graphics.Color.parseColor("#7d32a8"))
        )

        Image(
            painter = painterResource(R.drawable.bottom_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.graphicsLayer(
                rotationY = 180f // Xoay 180 độ
            )
        )
    }
}


suspend fun handleRegister(viewModel: RegisterViewModel, context: Context) {
    // Kiểm tra lỗi nhập liệu
    if (!viewModel.validateRegisterInput()) {
        viewModel.errorMessage = "Vui lòng nhập đầy đủ các trường"
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        return
    }

    // Kiểm tra định dạng email
    if (!viewModel.isEmailValid(viewModel.email)) {
        viewModel.errorMessage = "Email sai định dạng"
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        return
    }

    if (viewModel.phone.length != 10) {
        viewModel.errorMessage = "Số điện thoại sai định dạng"
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        return
    }

    // Kiểm tra tài khoản đã bị khóa
    if (viewModel.validateEmailLock()) {
        viewModel.errorMessage = "Tài khoản đã bị khóa !!"
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        return
    }

    // Kiểm tra tài khoản đã tồn tại
    if (!viewModel.validateExistingAccount()) {
        viewModel.errorMessage = "Tài khoản đã tồn tại !!"
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
        return
    }

    // Nếu không có lỗi, thêm user
    viewModel.addUser()

    // Chờ kết quả xử lý
    if (viewModel.errorMessage.isEmpty() && viewModel.isSuccess) {
        viewModel.openDialog.value = true
    } else {
        Toast.makeText(context, viewModel.errorMessage, Toast.LENGTH_SHORT).show()
    }
}



