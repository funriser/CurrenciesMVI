package com.revolut.converter.ui.error

interface ErrorHandler {
    fun getErrorMessage(throwable: Throwable): String
}