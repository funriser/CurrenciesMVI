package com.revolut.converter.di

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val context: Context) {

    @Provides
    @Singleton
    fun context(): Context = context

}