package com.revolut.converter.data.source

import com.revolut.converter.data.exceptions.NetworkException
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class ApiHandler @Inject constructor() {

    fun <T> handleRequestSingle(rawRequest: Single<Response<T>>): Single<T> {
        return rawRequest.flatMap {
            if (!isResponseValid(it)) {
                Single.error(NetworkException())
            } else {
                val data = it.body()
                Single.just(data)
            }
        }
    }

    private fun isResponseValid(response: Response<*>): Boolean {
        return response.isSuccessful && response.body() != null
    }

}