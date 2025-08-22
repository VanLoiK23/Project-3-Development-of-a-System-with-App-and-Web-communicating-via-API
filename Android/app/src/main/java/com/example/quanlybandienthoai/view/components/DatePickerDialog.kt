package com.example.quanlybandienthoai.view.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun DatePickerDialog(
    mutableDate: MutableState<String>,
    context: Context,
    calendar: Calendar,
    maxDate: Long?
): DatePickerDialog {
    val year: Int = calendar.get(Calendar.YEAR)
    val month: Int = calendar.get(Calendar.MONTH)
    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.add(Calendar.YEAR, -18)

//    val datePickerDialog = DatePickerDialog(
//        context,
//        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
//            mutableDate.value = "${
//                if (month < 9) {
//                    "0" + month.plus(1)
//                } else {
//                    month + 1
//                }
//            }/${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/$year"
//        }, year, day, month
//    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            mutableDate.value = "${if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth}/" +
                    "${if (month < 9) "0${month + 1}" else (month + 1)}/$year"
        },
        year,
        month,        // ✅ đúng: month ở đây
        day           // ✅ đúng: day ở đây
    )

    maxDate?.let { datePickerDialog.datePicker.maxDate = maxDate }

    return datePickerDialog
}

fun showBirthDatePickerDialog(
    context: Context,
    timestamp: Long?,
    onDateSelected: (String) -> Unit
): DatePickerDialog {
    val calendar = Calendar.getInstance()
    if (timestamp != null) {
        calendar.timeInMillis = timestamp
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val selectedCal = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            onDateSelected(sdf.format(selectedCal.time))
        },
        year, month, day
    )

    datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
    return datePickerDialog
}

