package com.example.quanlybandienthoai.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun IdentifyDialog(
    openDialog: MutableState<Boolean>,
    title: String = "Identify",
    description: String = "Action required",
    confirmBtnText: String = "Dismiss",
    confirmNavFn: () -> Unit = {}
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            dismissButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    )
                ) {
                    Text("Cancel")
                }
            },
            title = {
                Text(title)
            },
            text = {
                Column() {
                    Text(description, Modifier.padding(top = 10.dp))
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        openDialog.value = false
                        confirmNavFn()
                    }
                ) {
                    Text(confirmBtnText)
                }
            }
        )
    }
}