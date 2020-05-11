package com.funrisestudio.converter

import android.app.Application
import com.funrisestudio.converter.core.di.AndroidModule
import com.funrisestudio.converter.core.di.AppComponent
import com.funrisestudio.converter.core.di.DaggerAppComponent

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