package com.example.quanlybandienthoai.view.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quanlybandienthoai.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.launch


@Composable
fun ForgotPasswordDialog(viewModel: ForgotPasswordViewModel) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope() // CoroutineScope nhớ trạng thái

    if (viewModel.showForgotPasswordDialog) {
        AlertDialog(
            shape = RoundedCornerShape(15.dp),
            onDismissRequest = { viewModel.showForgotPasswordDialog = false },
            title = { Text("Quên mật khẩu") },
            text = {
                Column {
                    Text("Nhập email của bạn để đặt lại mật khẩu")

                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = {
                            if (viewModel.emailError) viewModel.emailError = false
                            viewModel.email = it
                        },
                        label = { androidx.compose.material3.Text("Nhập email") },
                        placeholder = { androidx.compose.material3.Text("Type your email...") },
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = "User")
                        },
                        trailingIcon = {
                            if (viewModel.email.isNotEmpty()) {
                                IconButton(onClick = { viewModel.email = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear text")
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                            coroutineScope.launch {
                                handleForgot(viewModel, context)
                            }
                        }),
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(30.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(android.graphics.Color.parseColor("#7d32a8")),
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.Blue
                        ),
                        isError = viewModel.emailError
                    )

                    if (viewModel.errorMessage.isNotEmpty()) {
                        androidx.compose.material3.Text(
                            text = viewModel.errorMessage,
                            color = Color.Red,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            handleForgot(viewModel, context)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(android.graphics.Color.parseColor("#7d32a8"))
                    ),
                    shape = RoundedCornerShape(10.dp),
                    enabled = !viewModel.isLoading, contentPadding = PaddingValues(5.dp)
                ) {
                    if (viewModel.isLoading) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = Color.White,
                        )
                    } else {
                        Text("Gửi Email", color = Color.White)
                    }
                }
            },
            dismissButton = {
                Button(
                    onClick = { viewModel.showForgotPasswordDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(android.graphics.Color.parseColor("#7d32a8"))
                    ),
                    shape = RoundedCornerShape(10.dp), contentPadding = PaddingValues(5.dp)
                ) {
                    Text("Hủy", color = Color.White)
                }
            }
        )
    }
}

suspend fun handleForgot(viewModel: ForgotPasswordViewModel, context: Context) {
    if (viewModel.validateForgotInput()) {
        viewModel.isLoading = true
        val user = viewModel.getUserByEmail(viewModel.email) // Gọi suspend function trong coroutine
        if (user != null) {
            viewModel.sendPasswordResetEmail(viewModel.email)
            viewModel.errorMessage = ""
            viewModel.showForgotPasswordDialog = false
            viewModel.openDialog.value = true

            Toast.makeText(context, "Đã gửi email reset password vào email ${viewModel.email}!", Toast.LENGTH_SHORT).show()
            viewModel.email=""

        } else {
            Toast.makeText(context, "Tài khoản email không tồn tại!", Toast.LENGTH_SHORT).show()
        }
        viewModel.isLoading = false
    } else {
        Toast.makeText(context, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
    }
}
