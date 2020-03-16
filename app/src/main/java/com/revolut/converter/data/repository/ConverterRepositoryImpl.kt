package com.revolut.converter.data.repository

import com.revolut.converter.data.source.converter.ConverterLocalSource
import com.revolut.converter.data.source.converter.ConverterRemoteSource
import com.revolut.converter.domain.entity.ExchangeRates
import com.revolut.converter.domain.repository.ConverterRepository
import io.reactivex.Single
import javax.inject.Inject

class ConverterRepositoryImpl @Inject constructor(
    private val converterRemoteSource: ConverterRemoteSource,
    private val converterLocalSource: ConverterLocalSource
) : ConverterRepository {

    override fun getLatestRates(baseCurrency: String, forceReload: Boolean): Single<ExchangeRates> {
        return if (!forceReload && converterLocalSource.hasActualData(baseCurrency)) {
            converterLocalSource.getLatestRates(baseCurrency)
        } else {
            converterRemoteSource.getLatestRates(baseCurrency)
                .doOnSuccess {
                    converterLocalSource.saveRates(it)
                }
        }
    }

}