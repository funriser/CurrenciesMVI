package com.revolut.converter.ui

import android.text.InputFilter
import android.text.Spanned

class BaseCurrencyLengthFilter: InputFilter {

    companion object {
        private const val DIGITS = "0123456789,."
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
        //Put "0" if user is trying to delete the sequence
        //or if user is trying to replace whole sequence with non-digit buffer
        if (isClearIntent(source, start, end, dest, dstart, dend) ||
            (isReplaceIntent(source, dest) && !isSourceAccepted(source))
        ) {
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

    private fun isClearIntent(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): Boolean {
        return (start == 0 && end == 0 &&
                (dend - dstart) == dest.length &&
                source.toString() == "")
    }

    private fun isReplaceIntent(source: CharSequence, dest: Spanned): Boolean {
        return source.length >= dest.length
    }

    private fun isSourceAccepted(source: CharSequence): Boolean {
        return source.find { !DIGITS.contains(it) } == null
    }

}