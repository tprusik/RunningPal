package com.example.runningpal.di

import com.example.runningpal.repositories.*
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.MessageViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinComponent
import org.koin.dsl.module

object ServiceModule : KoinComponent {

    val appModule = module {

        single { FirebaseAuth.getInstance() }
        single<IRunRepository> { RunRepository() }
        single<IRunStatisticsRepository> { RunStatisticsRepository() }
        single<IRunnersRepository> { RunnersRepository() }
        single<IMessageRepository> { MessageRepository() }

        viewModel { MainViewModel(get()) }

        viewModel { StatisticsViewModel(get()) }

        viewModel { RunnersViewModel(get()) }

        viewModel { MessageViewModel(get())

        }
    }
}
