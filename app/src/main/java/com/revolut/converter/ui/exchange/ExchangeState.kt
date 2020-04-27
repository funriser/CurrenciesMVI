package com.revolut.converter.ui.exchange

import android.os.Parcelable
import com.revolut.converter.domain.entity.ConvertedCurrency
import com.revolut.converter.domain.entity.Currency
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class ExchangeState(
    val from: ExchangeInput,
    val to: ExchangeInput
): Parcelable

@Parcelize
data class ExchangeInput(
    val currency: Currency,
    val amount: BigDecimal
): Parcelable {

    companion object {

        fun fromConvertedCurrency(currency: ConvertedCurrency): ExchangeInput {
            return ExchangeInput(currency.currency, currency.amount)
        }

    }

}