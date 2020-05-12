package com.funrisestudio.converter.data.repository

import com.funrisestudio.converter.data.source.converter.RatesLocalSource
import com.funrisestudio.converter.data.source.converter.RatesRemoteSource
import com.funrisestudio.converter.domain.entity.ExchangeRates
import com.funrisestudio.converter.domain.repository.RatesRepository
import io.reactivex.Single
import javax.inject.Inject

/**
 * Fetches latest currency rates from local or remote data source
 *
 * Uses local source (memory) if it has actual data and force reload
 * is not specified explicitly
 */
class RatesRepositoryImpl @Inject constructor(
    private val ratesRemoteSource: RatesRemoteSource,
    private val ratesLocalSource: RatesLocalSource
) : RatesRepository {

    override fun getLatestRates(baseCurrency: String, forceReload: Boolean): Single<ExchangeRates> {
        return if (!forceReload && ratesLocalSource.hasActualData(baseCurrency)) {
            ratesLocalSource.getLatestRates(baseCurrency)
        } else {
            ratesRemoteSource.getLatestRates(baseCurrency)
                .doOnSuccess {
                    ratesLocalSource.saveRates(it)
                }
        }
    }

}