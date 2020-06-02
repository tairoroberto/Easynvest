package br.com.tairoroberto.easynvest.core.extensions

import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import br.com.tairoroberto.easynvest.core.AppUtil
import br.com.tairoroberto.easynvest.core.AppUtil.clearMask
import br.com.tairoroberto.easynvest.core.AppUtil.defaultLocale
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val DATE_MASK = "##/##/####"

fun EditText.moneyMasK() {
    var current: String? = null
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s.toString() != current) {
                this@moneyMasK.removeTextChangedListener(this)
                current = AppUtil.textToMonetaryValue(
                    if (TextUtils.isEmpty(s)) "0" else AppUtil.removeCurrencySymbol(s.toString())
                )
                val result = "R$ $current"
                this@moneyMasK.setText(result)

                if (Build.VERSION.SDK_INT >= 28 && result.length <= 16) {
                    this@moneyMasK.setSelection(result.length)
                } else if (Build.VERSION.SDK_INT < 28 && result.length <= 15) {
                    this@moneyMasK.setSelection(result.length)
                }

                this@moneyMasK.addTextChangedListener(this)
            }
        }
    })
}

fun EditText.unMaskMoney(): Double {
    return try {
        (this.text.toString().replace(".", "")
            .replace(",", ".").replace("R$", "").trim { it <= ' ' }).toDouble()
    } catch (e: NumberFormatException) {
        0.0
    }
}

fun EditText.setCurrentDate() {
    val f = SimpleDateFormat("dd/MM/yyyy", AppUtil.defaultLocale)
    f.isLenient = false
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_MONTH, 1)
    this.setText(f.format(calendar.time))
}

fun EditText.dateMask() {
    var isUpdating = false
    val f = SimpleDateFormat("dd/MM/yyyy", AppUtil.defaultLocale)
    f.isLenient = false

    this.addTextChangedListener(object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (isUpdating) {
                return
            }

            isUpdating = true

            var str = clearMask(s.toString())
            this@dateMask.text.clear()

            if (str.length >= 8) {
                str = str.subSequence(0, 8).toString()
            }

            for (i in str.indices) {
                if (i == 2) {
                    this@dateMask.text?.append("/")
                }
                if (i == 4) {
                    this@dateMask.text?.append("/")
                }
                this@dateMask.text?.append(str[i])
            }

            try {
                this@dateMask.setText(
                    setMask(
                        s.toString(),
                        DATE_MASK
                    )
                )

            } catch (e: ParseException) {
                this@dateMask.setText(
                    setMask(
                        s.toString(),
                        DATE_MASK
                    )
                )
            }

            isUpdating = false
            this@dateMask.setSelection(this@dateMask.text.length)
        }
    })
}

fun EditText.getDateFormatted(): String {
    var dataForm: String

    try {
        val f = SimpleDateFormat(
            "dd/MM/yyyy",
            defaultLocale
        )
        val presenter = SimpleDateFormat(
            "yyyy-MM-dd",
            defaultLocale
        )
        dataForm = try {
            val date = f.parse(this.text.toString())
            presenter.format(date)
        } catch (e: ParseException) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            presenter.format(calendar.time)
        }
    } catch (e: Exception) {
        dataForm = ""
        Log.e("TAG", "formatDate", e)
    }

    return dataForm
}

fun setMask(mask: String, text: String): String {
    val maskCurrent = StringBuilder()
    var i = 0
    for (m in mask.toCharArray()) {
        if (m != '#') {
            maskCurrent.append(m)
            continue
        }
        try {
            maskCurrent.append(text[i])
        } catch (e: Exception) {
            break
        }

        i++
    }
    return maskCurrent.toString()
}
