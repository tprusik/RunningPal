package com.example.runningpal

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


class BaseApplication : Application() {

    val appModule = module {

        //single instance of something
        single { FirebaseAuth.getInstance() }
    }


    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {


            modules(appModule)
        }
    }


}