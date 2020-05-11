package com.funrisestudio.converter.ui.error

import android.content.Context
import com.funrisestudio.converter.R
import com.funrisestudio.converter.data.exceptions.NetworkException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ConverterErrorHandler @Inject constructor(
    private val context: Context
) : ErrorHandler {

    override fun getErrorMessage(throwable: Throwable): String {
        val res = when (throwable) {
            is NetworkException, is SocketTimeoutException, is UnknownHostException -> {
                R.string.error_network
            }
            else -> R.string.error_unknown
        }
        return context.getString(res)
    }

}