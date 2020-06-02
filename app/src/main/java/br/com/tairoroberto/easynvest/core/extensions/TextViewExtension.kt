package br.com.tairoroberto.easynvest.core.extensions

import android.widget.TextView
import br.com.tairoroberto.easynvest.core.AppUtil
import java.text.SimpleDateFormat
import java.util.*

fun TextView.formatValueCurrency(value: Double?) {
    this.text = "R$ ${String.format(Locale("pt", "BR"), "%.2f", value)}"
}

fun TextView.formatValue(value: Double?) {
    this.text = "${String.format(Locale("pt", "BR"), "%.2f", value)}%"
}

fun TextView.parseDate(stringDate: String) {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", AppUtil.defaultLocale)
    val format2 = SimpleDateFormat("dd/MM/yyyy", AppUtil.defaultLocale)
    val date = format.parse(stringDate)
    this.text = format2.format(date)
}