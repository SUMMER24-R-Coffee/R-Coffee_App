package datlowashere.project.rcoffee.utils

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object FormatterHelper {
    private val outputDateTimeFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US)

    fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
    }


    fun formatDateTime(date: Date): String {
        return outputDateTimeFormat.format(date)
    }
}