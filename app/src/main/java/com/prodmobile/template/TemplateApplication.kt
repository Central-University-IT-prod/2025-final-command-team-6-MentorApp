package com.prodmobile.template

import android.app.Application
import com.prodmobile.template.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MobileTemplateApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MobileTemplateApplication)
            modules(appModule)
        }
    }
}