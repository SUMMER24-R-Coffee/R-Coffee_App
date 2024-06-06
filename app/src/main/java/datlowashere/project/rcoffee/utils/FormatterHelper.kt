package datlowashere.project.rcoffee.utils

import java.text.NumberFormat
import java.util.Locale

object FormatterHelper {
    fun formatCurrency(amount: Double): String {
        return NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(amount)
    }
}