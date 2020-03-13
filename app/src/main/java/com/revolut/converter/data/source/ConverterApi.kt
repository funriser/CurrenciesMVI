package com.revolut.converter.data.source

import com.revolut.converter.data.dto.CurrencyRatesDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterApi {

    @GET("api/android/latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Single<CurrencyRatesDto>

}