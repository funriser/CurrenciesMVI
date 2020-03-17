package com.revolut.converter.ui

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object DecimalFormat {

    private val symbolsFormat = DecimalFormatSymbols.getInstance()
    private val decimalFormat = DecimalFormat.getInstance()

    const val DIGITS = "0123456789"
    val FRACTION_SIGN = symbolsFormat.decimalSeparator
    val GROUP_SIGN = symbolsFormat.groupingSeparator
    val ACCEPTED_INPUT = DIGITS + FRACTION_SIGN

    fun toDecimalString(source: String, cutFractionZero: Boolean = false): String {
        val fractionalSignInd = source.indexOf(FRACTION_SIGN)
        if (fractionalSignInd == 0) {
            return source
        }
        val s = if (!cutFractionZero || fractionalSignInd == -1) {
            source
        } else {
            val fractionPart = source.substring(fractionalSignInd + 1, source.length)
            if (fractionPart == "00" || fractionPart == "0") {
                source.substring(0, fractionalSignInd)
            } else if (fractionPart.last() == '0') {
                source.substring(0, fractionalSignInd + 2)
            } else {
                source
            }
        }
        //index where the part of the number without fraction ends
        val integralPartEnd = if (fractionalSignInd != -1) {
            fractionalSignInd
        } else {
            s.length
        }
        val integralPart = s.substring(0, integralPartEnd)
        //remove group signs from integral part
        val pureIntegral = integralPart.replace(GROUP_SIGN.toString(), "")
        //add grouping symbols to the new number
        val groupIntegral = String.format("%,d", pureIntegral.toLong())
        //add fraction part from source sequence
        return groupIntegral + s.substring(integralPartEnd, s.length)
    }

    fun toDecimalString(amount: BigDecimal, cutFractionZero: Boolean = false): String {
        return toDecimalString(decimalFormat.format(amount), cutFractionZero)
    }

    fun fromDecimalString(s: String): BigDecimal {
        val pureStr = s.replace(GROUP_SIGN.toString(), "")
            .replace(FRACTION_SIGN.toString(), ".")
        return pureStr.toBigDecimal()
    }

}