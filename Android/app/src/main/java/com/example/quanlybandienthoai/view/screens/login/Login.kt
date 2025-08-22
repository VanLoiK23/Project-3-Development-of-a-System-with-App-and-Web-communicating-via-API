package com.example.quanlybandienthoai.view.screens.login

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextDecoration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quanlybandienthoai.R
import com.example.quanlybandienthoai.view.components.SuccessDialog
import com.example.quanlybandienthoai.view.navigation.Screen
import com.example.quanlybandienthoai.viewmodel.AppViewModel
import com.example.quanlybandienthoai.viewmodel.ForgotPasswordViewModel
import com.example.quanlybandienthoai.viewmodel.LoginViewModel
import com.example.quanlybandienthoai.viewmodel.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onSuccessfulLogin: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    appViewModel: AppViewModel = viewModel(LocalContext.current as ComponentActivity),
    viewModel: LoginViewModel = viewModel(LocalContext.current as ComponentActivity),
    forgotViewModel: ForgotPasswordViewModel = viewModel(LocalContext.current as ComponentActivity),
    mainViewModel: MainViewModel = viewModel(LocalContext.current as ComponentActivity)
) {


    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(android.graphics.Color.parseColor("#ffffff")))
            .clickable(
                interactionSource = remember { MutableInteractionSource() }, indication = null
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }, horizontalAlignment = Alignment.CenterHorizontally
    ) {

        suspend fun handleLogin() {
            if (viewModel.validateLoginInput()) {
                if (viewModel.isExistInFireBase()) {
                    if (viewModel.authenticateLogin()) {
                        appViewModel.setCurrentUserId(viewModel.getCurrentUserId())
                        viewModel.password = ""
                        viewModel.email = ""
                        viewModel.errorMessage = ""
                        onSuccessfulLogin(appViewModel.isAdmin)
                        if (appViewModel.isAdmin) {
                            mainViewModel.screens = MutableStateFlow(
                                listOf(
                                    Screen.HomeAdmin,
                                    Screen.ManageProduct.Product,
                                    Screen.ManageBrand.Brand,
                                    Screen.ManageOrder.Order,
                                    Screen.ManageUser.User
                                )
                            )
                        } else {
                            mainViewModel.screens = MutableStateFlow(
                                listOf(
                                    Screen.Home,
                                    Screen.Shop,
                                    Screen.Cart,
                                    Screen.Profile
                                )
                            )
                        }
                        viewModel.onLoginSuccess(viewModel.getCurrentUserId().toInt())
                        appViewModel.isLoginSuccess.value = true
                    } else {
                        Toast.makeText(
                            context, "Thông tin xác thực không chính xác!", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    context, if (viewModel.errorMessage.contains("khóa")) {
                        viewModel.errorMessage
                    } else {
                        "Vui lòng nhập đầy đủ thông tin đăng nhập!"
                    }, Toast.LENGTH_SHORT
                ).show()
            }
        }

        Image(
            painter = painterResource(R.drawable.top_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.height(150.dp)
        )

        Text(
            text = "Welcome to My App",
            fontSize = 30.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Bold,
            color = Color(android.graphics.Color.parseColor("#7d32a8"))
        )

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = {
                if (viewModel.emailError) viewModel.emailError = false
                viewModel.email = it
            },
            label = { Text("Nhập email") },
            placeholder = { Text("Type your email...") },
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            leadingIcon = {
                Icon(
                    Icons.Default.Email, contentDescription = "User"
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
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.moveFocus(FocusDirection.Down) }
            ),

            singleLine = true, // Chỉ nhập 1 dòng
            maxLines = 1, // Giới hạn dòng tối đa
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(30.dp), // Bo góc
            colors = OutlinedTextFieldDefaults.colors( // Dùng nếu là `OutlinedTextField`
                focusedBorderColor = Color(
                    android.graphics.Color.parseColor(
                        "#7d32a8"
                    )
                ), unfocusedBorderColor = Color.Gray, cursorColor = Color.Blue
            ),
            isError = viewModel.emailError
        )


        OutlinedTextField(
            value = viewModel.password,
            onValueChange = {
                if (viewModel.passwordError) viewModel.passwordError = false
                viewModel.password = it
            },
            label = { Text("Nhập password") },
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
                coroutineScope.launch {
                    handleLogin()
                }
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
                .padding(16.dp),
            isError = viewModel.passwordError,
        )

        Row(
            Modifier
                .fillMaxWidth()
                .offset(x = (-20).dp), horizontalArrangement = Arrangement.End
        ) {
//            Text("Don't remember password? Click here",
//                Modifier
//                    .padding(top = 10.toAdaptiveDp())
//                    .clickable() { onForgotClick() })

            Text(
                text = "Don't remember password? Click here",
                Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .clickable { forgotViewModel.showForgotPasswordDialog = true },
                fontSize = 14.sp,
                color = Color(android.graphics.Color.parseColor("#7d32a8"))
            )
        }

        if (viewModel.errorMessage.isNotEmpty()) {
            Text(
                text = viewModel.errorMessage,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    handleLogin()
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
                CircularProgressIndicator(color = Color.White, modifier = Modifier.padding(0.dp))
            } else {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

//        Text(
//            text = "Don't remember password? Click here",
//            Modifier
//                .padding(top = 8.dp, bottom = 8.dp)
//                .clickable { showForgotPasswordDialog.value = true },
//            fontSize = 14.sp,
//            color = Color(android.graphics.Color.parseColor("#7d32a8"))
//        )


        ForgotPasswordDialog(forgotViewModel)

        SuccessDialog(
            openDialog = forgotViewModel.openDialog,
            "Success !!",
            forgotViewModel.messageDialog,
            "Xác nhận"
        ) {
            forgotViewModel.openDialog.value = false
            forgotViewModel.openGmailApp(context)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(5.dp))


            Text(
                text = "Don't have a account ? Create here!!",
                Modifier
                    .padding(top = 10.dp, bottom = 15.dp)
                    .clickable() { onRegisterClick() },
                fontSize = 16.sp,
                color = Color(android.graphics.Color.parseColor("#7d32a8")),
                textDecoration = TextDecoration.Underline
            )


        }

//        Row {
//            Image(
//                painter = painterResource(R.drawable.google),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//            Image(
//                painter = painterResource(R.drawable.twitter),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//            Image(
//                painter = painterResource(R.drawable.facebook),
//                contentDescription = null,
//                Modifier.padding(8.dp)
//            )
//
//        }

        Image(
            painter = painterResource(R.drawable.bottom_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
    }
}

