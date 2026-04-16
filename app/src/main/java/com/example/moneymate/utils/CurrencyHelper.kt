package com.example.moneymate.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyHelper {
    private val localeID = Locale("in", "ID")

    fun formatToRupiah(amount: Long): String {
        val formatter = NumberFormat.getCurrencyInstance(localeID)
        return formatter.format(amount).replace(",00", "")
    }
}