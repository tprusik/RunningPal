package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.runningpal.repositories.IRunnersRepository

class RunnersViewModel(val repo : IRunnersRepository) : ViewModel() {

    val allRunners = repo.getAllRunners()


}