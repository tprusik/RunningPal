package com.example.runningpal.others
import android.app.Application
import android.content.Intent
import com.example.runningpal.BuildConfig
import com.example.runningpal.di.ServiceModule
import com.example.runningpal.services.ListenerService
import org.koin.core.context.startKoin
import timber.log.Timber


class BaseApplication : Application() {

        override fun onCreate() {
            super.onCreate()

            if(BuildConfig.DEBUG) { Timber.plant(Timber.DebugTree()) }

            startKoin { modules(ServiceModule.appModule) }

        }


    }
