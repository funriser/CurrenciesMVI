package com.funrisestudio.converter.domain.repository

import com.funrisestudio.converter.domain.entity.ExchangeRates
import io.reactivex.Single

interface ConverterRepository {

    fun getLatestRates(baseCurrency: String, forceReload: Boolean = true): Single<ExchangeRates>

}