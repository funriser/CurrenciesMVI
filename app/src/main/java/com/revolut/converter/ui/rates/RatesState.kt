package com.revolut.converter.ui.rates

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RatesState(
    val baseCurrency: String,
    val amount: String
): Parcelable