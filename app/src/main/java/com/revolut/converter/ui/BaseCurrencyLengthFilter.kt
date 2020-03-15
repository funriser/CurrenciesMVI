package com.revolut.converter.ui

import android.text.InputFilter
import android.text.Spanned

class BaseCurrencyLengthFilter: InputFilter {

    companion object {
        private const val DIGITS_LIMIT = 9
        private const val FRACTION_LIMIT = 2
    }

    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (dest == null || source == null) {
            return null
        }
        //Put "0" if user is trying to delete last char from sequence
        if (dest.length == 1 && source.toString() == "" && start == 0 && end == 0) {
            return "0"
        }
        //total digits in destination (fraction included)
        var digitsCount = 0
        var fractionCount = 0
        var fractionPartInd = -1
        dest.forEachIndexed { i, c ->
            if (c != DecimalFormat.FRACTION_SIGN && c != DecimalFormat.GROUP_SIGN) {
                digitsCount ++
            }
            if (fractionCount != 0) {
                fractionCount ++
            }
            if (c == DecimalFormat.FRACTION_SIGN) {
                fractionCount ++
                fractionPartInd = i
            }
        }
        //skip new value if total number of digits is too large
        if (digitsCount >= DIGITS_LIMIT) {
            return ""
        }
        //skip new value if fraction part is too large
        if (dstart > fractionPartInd && fractionCount > FRACTION_LIMIT) {
            return ""
        }
        //skip new value if user trying to make new fraction part
        //that exceeds limit
        if (source == DecimalFormat.FRACTION_SIGN.toString() &&
            (dest.length - dstart) > FRACTION_LIMIT) {
            return ""
        }
        return null
    }

}