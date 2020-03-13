package com.revolut.converter.domain

import java.math.BigDecimal
import java.math.RoundingMode

const val SCALE = 2
val ROUND_MODE = RoundingMode.HALF_UP
val ZERO = BigDecimal("0.0").defScale()

operator fun BigDecimal.div(other: BigDecimal) = this.divide(other, SCALE, ROUND_MODE)

operator fun BigDecimal.times(other: BigDecimal) = this.multiply(other)

fun BigDecimal.defScale(): BigDecimal = this.setScale(SCALE, ROUND_MODE)

fun Double?.toDecimal(): BigDecimal = this?.toBigDecimal()?.defScale() ?: ZERO

fun Int.toDecimal(): BigDecimal = this.toBigDecimal().defScale()

fun String?.toDecimal(): BigDecimal = this?.toBigDecimal()?.defScale()?: ZERO.defScale()

fun BigDecimal?.orZero() = this?: ZERO