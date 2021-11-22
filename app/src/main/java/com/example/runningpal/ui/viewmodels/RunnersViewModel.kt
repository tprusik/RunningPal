package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.runningpal.db.User
import com.example.runningpal.repositories.IRunnersRepository

class RunnersViewModel(val repo : IRunnersRepository) : ViewModel() {

    val allRunners = repo.getAllRunners()
    val user  =  repo.getCurrentUser()

    fun updateUser(user : User) = repo.updateUser(user)

}