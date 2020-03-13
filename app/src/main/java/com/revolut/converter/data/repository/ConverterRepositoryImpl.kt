package com.revolut.converter.data.repository

import com.revolut.converter.data.source.ConverterApi
import com.revolut.converter.domain.repository.ConverterRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterApi: ConverterApi
) : ConverterRepository {

    override fun getLatestRates(baseCurrency: String): Single<Map<String, String>> {
        return converterApi.getCurrencyRates(baseCurrency)
            .map {
                it.rates
            }
    }

}