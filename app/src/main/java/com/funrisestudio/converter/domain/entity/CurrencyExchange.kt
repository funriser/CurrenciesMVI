package com.funrisestudio.converter.domain.entity

import android.os.Parcelable
import com.funrisestudio.converter.ui.delegate.CurrencyItem
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * Data class that holds currency description
 */
@Parcelize
data class Currency(
    val code: String,
    val name: String,
    val image: Int
): Parcelable

/**
 * Specifies exchange rate for currency
 */
data class CurrencyRate(
    val currency: Currency,
    val rate: BigDecimal
)

/**
 * Holds all information needed to perform exchange
 * baseCurrency is for currency we want to exchange
 */
data class ExchangeRates(
    val baseCurrency: Currency,
    val rates: List<CurrencyRate>
)


/**
 * Holds information about already performed exchange
 * amount specifies calculated price of purchase
 */
open class ConvertedCurrency(
    val currency: Currency,
    val amount: BigDecimal
): CurrencyItem

/**
 * Info for base currency used for exchange
 * amount specifies how much currency we wanted to sell
 */
class BaseConvertedCurrency(
    currency: Currency,
    amount: BigDecimal
): ConvertedCurrency(currency, amount)