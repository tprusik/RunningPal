package com.example.runningpal
import android.app.Application
import android.content.Intent
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.runningpal.repositories.*
import com.example.runningpal.services.ListenerService
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.MessageViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber


class BaseApplication : Application() {

    val appModule = module {


        single { fun provideSharedPreferences(){} }

        //single instance of something
        single { FirebaseAuth.getInstance() }

        single  <IRunRepository> { RunRepository() }
        single  <IRunStatisticsRepository> { RunStatisticsRepository() }
        single  <IRunnersRepository> { RunnersRepository() }
        single  <IMessageRepository> { MessageRepository() }

        viewModel {
            MainViewModel(get())
        }

        viewModel  {
            StatisticsViewModel(get())
        }

        viewModel  {

            RunnersViewModel(get())
        }

        viewModel  {

            MessageViewModel(get())

        }
    }

        override fun onCreate() {
            super.onCreate()

            Intent(baseContext, ListenerService::class.java).also {
                baseContext.startService(it)
            }

            if (BuildConfig.DEBUG) {
                Timber.plant(Timber.DebugTree())
            }

            startKoin {
                modules(appModule)
            }
        }


    }
