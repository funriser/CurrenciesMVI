package com.revolut.converter.ui

import android.text.InputFilter
import android.text.Spanned

class BaseCurrencyLengthFilter: InputFilter {

    companion object {
        private const val DIGITS = "0123456789,."
        private const val BUFFER_LIMIT = 12
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
                    digitsCount ++
                }
                if (fractionCount != 0) {
                    fractionCount ++
                }
                if (c == DecimalFormat.FRACTION_SIGN) {
                    if (source == DecimalFormat.FRACTION_SIGN.toString()) {
                        return ""
                    }
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
        }
        //if formatted decimal comes to filter
        if (isFormatTextIntent(dest, source)) {
            return null
        }
        //trying to paste something to input
        if (isBufferInputIntent(dest, source)) {
            //how much symbols is replaced by buffer
            val cutLength = dend - dstart
            //if input is already out of bounds
            val buffLimit = if (dest.length - cutLength >= BUFFER_LIMIT) {
                0
            } else {
                BUFFER_LIMIT - dest.length + cutLength - 1
            }
            //apply changes to be sure that buffer is valid
            val processedBuffer = getProcessedBuffer(source, dest, dstart, buffLimit)
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
        return (start == 0 && end == 0 &&
                (dend - dstart) == dest.length &&
                source.toString() == "")
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

    private fun getProcessedBuffer(source: CharSequence, dest: Spanned, dstart: Int, lengthLimit: Int): String {
        val fractionPartInd = dest.indexOf(DecimalFormat.FRACTION_SIGN)
        val isAfterFractional = if (fractionPartInd == -1) {
            false
        } else {
            dstart > fractionPartInd
        }
        //after fraction part buffer is limited to 2 signs
        val fractionalPlaces = dest.length - dstart
        val limit = if (fractionPartInd != -1 && isAfterFractional
            && fractionalPlaces <= FRACTION_LIMIT) {
            fractionalPlaces
        } else {
            lengthLimit
        }
        if (limit <= 0) {
            return ""
        }
        val isFractionAllowed = dest.isEmpty()
        val resultBuffer = StringBuilder()
        run loop@ {
            source.forEach {
                //if symbol matches digit pattern and is supported
                //by fraction part
                if (DIGITS.contains(it) &&
                    !(it == DecimalFormat.FRACTION_SIGN && !isFractionAllowed) &&
                    !(it == DecimalFormat.GROUP_SIGN && isAfterFractional)
                ) {
                    resultBuffer.append(it)
                }
                if (resultBuffer.length >= limit) {
                    return@loop
                }
            }
        }
        return resultBuffer.toString()
    }

}