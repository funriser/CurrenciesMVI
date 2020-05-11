package com.funrisestudio.converter.domain

import java.math.BigDecimal
import java.math.RoundingMode

const val SCALE = 2
val ROUND_MODE = RoundingMode.HALF_UP

operator fun BigDecimal.div(other: BigDecimal): BigDecimal  = this.divide(other, SCALE, ROUND_MODE)

operator fun BigDecimal.times(other: BigDecimal): BigDecimal = this.multiply(other)

fun BigDecimal.defScale(): BigDecimal = this.setScale(SCALE, ROUND_MODE)