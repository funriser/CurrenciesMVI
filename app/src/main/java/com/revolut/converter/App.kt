package com.revolut.converter

import android.app.Application
import com.revolut.converter.core.di.AndroidModule
import com.revolut.converter.core.di.AppComponent
import com.revolut.converter.core.di.DaggerAppComponent

class App: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
            .builder()
            .androidModule(AndroidModule(this))
            .build()
    }

}