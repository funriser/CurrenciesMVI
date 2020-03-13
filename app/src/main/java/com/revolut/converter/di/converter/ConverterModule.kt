package com.revolut.converter.di.converter

import com.revolut.converter.data.repository.ConverterRepositoryImpl
import com.revolut.converter.domain.repository.ConverterRepository
import dagger.Binds
import dagger.Module

@Module
abstract class ConverterModule {

    @Binds
    @ConverterScope
    abstract fun converterRepository(
        converterRepositoryImpl: ConverterRepositoryImpl
    ) : ConverterRepository

}