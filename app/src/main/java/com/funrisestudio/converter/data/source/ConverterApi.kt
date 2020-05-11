package com.funrisestudio.converter.data.source

import com.funrisestudio.converter.data.dto.CurrencyRatesDto
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ConverterApi {

    @GET("api/android/latest")
    fun getCurrencyRates(@Query("base") baseCurrency: String): Single<Response<CurrencyRatesDto>>

}