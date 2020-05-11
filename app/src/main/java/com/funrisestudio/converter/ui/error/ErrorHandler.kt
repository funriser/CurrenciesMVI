package com.funrisestudio.converter.ui.error

interface ErrorHandler {
    fun getErrorMessage(throwable: Throwable): String
}