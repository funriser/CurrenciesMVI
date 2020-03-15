package com.revolut.converter

import com.revolut.converter.ui.DecimalFormat
import junit.framework.Assert.assertEquals
import org.junit.Test

class DecimalFormatTest {

    @Test
    fun `should group string number`() {
        val res = DecimalFormat.toDecimalString("10000")
        assertEquals("10,000", res)
    }

    @Test
    fun `should group string number and cut fractional when no fractional`() {
        val res = DecimalFormat.toDecimalString("10000", true)
        assertEquals("10,000", res)
    }

    @Test
    fun `should cut two zeros on end`() {
        val answer = DecimalFormat.toDecimalString("42.00", true)
        assertEquals("42", answer)
    }

    @Test
    fun `should cut one zero on end`() {
        val answer = DecimalFormat.toDecimalString("42.0", true)
        assertEquals("42", answer)
    }

    @Test
    fun `should cut zero for hundred part`() {
        val answer = DecimalFormat.toDecimalString("42.10", true)
        assertEquals("42.1", answer)
    }

}