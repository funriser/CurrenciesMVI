package com.funrisestudio.converter.ui.input

import android.text.InputFilter
import android.text.Spanned
import com.funrisestudio.converter.ui.DecimalFormat
import com.funrisestudio.converter.ui.DecimalFormat.DIGITS

/**
 * Filters base currency input
 * Accepts only digits and characters used for grouping
 * and separating decimals
 *
 * Limits total count of decimals
 *
 * Responsible for keeping decimal value in input
 * Filters duplicated fraction points and empty states
 *
 * Inspects and filters exchange buffer before performing copy/paste
 */
class BaseCurrencyLengthFilter : InputFilter {

    companion object {
        private const val DIGITS_LIMIT = 8
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
        if (isClearIntent(source, start, end, dest, dstart, dend)) {
            return "0"
        }
        //receive one symbol from keyboard
        if (isKeyInputIntent(dest, source)) {
            //total digits in destination (fraction included)
            var digitsCount = 0
            var fractionCount = 0
            var fractionPartInd = -1
            dest.forEachIndexed { i, c ->
                if (c != DecimalFormat.FRACTION_SIGN && c != DecimalFormat.GROUP_SIGN) {
                    digitsCount++
                }
                if (fractionCount != 0) {
                    fractionCount++
                }
                if (c == DecimalFormat.FRACTION_SIGN) {
                    //do nothing if we already have fraction sign
                    if (source == DecimalFormat.FRACTION_SIGN.toString()) {
                        return ""
                    }
                    fractionCount++
                    fractionPartInd = i
                }
            }
            //skip new value if total number of digits is too large
            if (digitsCount >= DIGITS_LIMIT) {
                return stubInput(dest, dstart, dend)
            }
            //skip new value if fraction part is too large
            if (dstart > fractionPartInd && fractionCount > FRACTION_LIMIT) {
                return ""
            }
            //skip new value if user trying to make new fraction part
            //that exceeds limit
            if (source == DecimalFormat.FRACTION_SIGN.toString() &&
                (dest.length - dstart) > FRACTION_LIMIT
            ) {
                return stubInput(dest, dstart, dend)
            }
        }
        //if formatted decimal comes to filter
        if (isFormatTextIntent(dest, source)) {
            return null
        }
        //trying to paste something to input
        if (isBufferInputIntent(dest, source)) {
            val destDigitsLen = countDigits(dest)
            //how much symbols is replaced by buffer
            val cutDigitsLen = countDigits(dest.substring(dstart, dend))
            //if input is already out of bounds
            val buffLimit = if (destDigitsLen - cutDigitsLen >= DIGITS_LIMIT) {
                0
            } else {
                DIGITS_LIMIT - destDigitsLen + cutDigitsLen
            }
            //apply changes to be sure that buffer is valid
            val processedBuffer = getProcessedBuffer(source, dest, dstart, dend, buffLimit)
            if (processedBuffer.isEmpty() && isReplaceAllIntent(dest, dstart, dend)) {
                return "0"
            }
            return processedBuffer
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
        val isDeleteIntent = start == 0 && end == 0 && source.toString() == ""
        val isDeletingLastSymbol = (dend - dstart) == dest.length
        val clearedDest = dest.removeRange(dstart until dend).toString()
        val isDeletingLastFraction = clearedDest == DecimalFormat.FRACTION_SIGN.toString()
        return (isDeleteIntent && (isDeletingLastSymbol || isDeletingLastFraction)) ||
                isDeletingLastSymbol && source.length == 1 && !DIGITS.contains(source)
    }

    private fun isKeyInputIntent(dest: Spanned, source: CharSequence): Boolean {
        return dest.isNotEmpty() && source.length == 1
    }

    private fun isFormatTextIntent(dest: Spanned, source: CharSequence): Boolean {
        return dest.isEmpty() && source.length > 1
    }

    private fun isBufferInputIntent(dest: Spanned, source: CharSequence): Boolean {
        return dest.isNotEmpty() && source.length > 1
    }

    private fun isReplaceAllIntent(dest: Spanned, dstart: Int, dend: Int): Boolean {
        return dend - dstart == dest.length
    }

    private fun stubInput(dest: Spanned, dstart: Int, dend: Int): String {
        return if (isReplaceAllIntent(dest, dstart, dend)) {
            "0"
        } else {
            ""
        }
    }

    private fun countDigits(seq: CharSequence): Int {
        return seq.count { DIGITS.contains(it) }
    }

    private fun getProcessedBuffer(
        source: CharSequence,
        dest: Spanned,
        dstart: Int,
        dend: Int,
        lengthLimit: Int
    ): String {
        val fractionPartInd = dest.indexOf(DecimalFormat.FRACTION_SIGN)
        val isAfterFractional = if (fractionPartInd == -1) {
            false
        } else {
            dstart > fractionPartInd
        }
        //after fraction part buffer is limited to 2 signs
        val fractionalPlaces = dest.length - dstart
        val limit = if (
            fractionPartInd != -1 && isAfterFractional
            && fractionalPlaces <= FRACTION_LIMIT
        ) {
            fractionalPlaces
        } else {
            lengthLimit
        }
        if (limit <= 0) {
            return ""
        }
        val isFractionAllowed = dest.isEmpty() || isReplaceAllIntent(dest, dstart, dend)
        val resultBuffer = StringBuilder()
        run loop@{
            var resultLength = 0
            var hasFraction = false
            source.forEach {
                //if symbol matches digit pattern and is supported
                //by fraction part
                if ((DIGITS.contains(it) || it == DecimalFormat.FRACTION_SIGN) &&
                    !(it == DecimalFormat.FRACTION_SIGN && !isFractionAllowed) &&
                    !(it == DecimalFormat.GROUP_SIGN && isAfterFractional)
                ) {
                    //if fraction is absent in the result buffer
                    if (!(hasFraction && it == DecimalFormat.FRACTION_SIGN)) {
                        resultBuffer.append(it)
                        if (it != DecimalFormat.FRACTION_SIGN) {
                            resultLength++
                        } else {
                            hasFraction = true
                        }
                    }
                }
                if (resultLength >= limit) {
                    return@loop
                }
            }
        }
        val resultStr = resultBuffer.toString()
        return if (resultStr == DecimalFormat.FRACTION_SIGN.toString()) {
            ""
        } else {
            resultStr
        }
    }

}