package com.example.quanlybandienthoai.view.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun SuccessDialog(
    openDialog: MutableState<Boolean>,
    title: String = "Success",
    description: String = "Action successful",
    confirmBtnText: String = "Dismiss",
    confirmNavFn: () -> Unit = {}
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                confirmNavFn()
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
                    modifier = Modifier.fillMaxWidth(),
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