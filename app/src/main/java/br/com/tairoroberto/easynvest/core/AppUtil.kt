package br.com.tairoroberto.easynvest.core

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.NumberFormat
import java.util.*

object AppUtil {
    private val currencyInstance: NumberFormat
        get() = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    val defaultLocale: Locale
        get() = Locale("pt", "BR")

    internal fun textToMonetaryValue(s: CharSequence?): String {
        return if (s.toString() != "0,0" && "" != s.toString()) {
            formatNumberToInputMoney(s.toString())
        } else "0,00"
    }

    private fun formatNumberToInputMoney(cleanString: String): String {
        if (TextUtils.isEmpty(cleanString)) {
            return ""
        }

        val parsed = java.lang.Double.parseDouble(cleanString.replace("[^0-9]".toRegex(), ""))
        return getFormattedValue(parsed / 100)
            .replace("R$", "")
    }

    private fun getFormattedValue(value: Any?): String {
        return if (Build.VERSION.SDK_INT >= 28) {
            currencyInstance.format(value).replace("\\s+".toRegex(), "")
        } else currencyInstance.format(value)
    }

    fun removeCurrencySymbol(value: String): String {
        return value.replace("R$", "").trim { it <= ' ' }
    }

    fun clearMask(s: String): String {
        return s
            .replace("[-]".toRegex(), "")
            .replace("[/]".toRegex(), "")
            .replace("[(]".toRegex(), "")
            .replace("[ ]".toRegex(), "")
            .replace("[:]".toRegex(), "")
            .replace("[)]".toRegex(), "")
            .replace("[^0-9]*".toRegex(), "")
    }

    fun hideKeyboard(view: View?) {
        if (view != null) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
