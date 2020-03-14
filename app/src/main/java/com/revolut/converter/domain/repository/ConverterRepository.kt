package com.revolut.converter.domain.repository

import com.revolut.converter.domain.entity.ExchangeRates
import io.reactivex.Single

interface ConverterRepository {

    fun getLatestRates(baseCurrency: String, forceReload: Boolean = true): Single<ExchangeRates>

}