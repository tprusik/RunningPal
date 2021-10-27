package com.example.runningpal
import android.app.Application
import com.example.runningpal.repositories.IRunRepository
import com.example.runningpal.repositories.RunRepository
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


class BaseApplication : Application() {

    val appModule = module {

        //single instance of something
        single { FirebaseAuth.getInstance() }

        single<IRunRepository> { RunRepository() }

        viewModel {
            MainViewModel(get())
        }
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
