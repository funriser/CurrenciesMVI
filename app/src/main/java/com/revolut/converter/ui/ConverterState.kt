package com.revolut.converter.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ConverterState(val baseCurrency: String, val amount: String): Parcelable