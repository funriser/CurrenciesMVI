package com.revolut.converter.domain.repository

import io.reactivex.Single

interface ConverterRepository {

    fun getLatestRates(baseCurrency: String): Single<Map<String, String>>

}