package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningpal.db.User
import com.example.runningpal.repositories.IRunnersRepository

class RunnersViewModel(val repo : IRunnersRepository) : ViewModel() {

    val runner = MutableLiveData<User>()

    val selectedItem: LiveData<User> get() = runner

    fun selectItem(item: User) {
        runner.value = item
    }


    fun getSelectedRunner(id : List<String>) = repo.getSelectedRunners(id)

    fun retRunner() : LiveData<User> { return runner}

    fun getRunner(id:String) = repo.getRunner(id)

    val allRunners = repo.getAllRunners()

    val user  =  repo.getCurrentUser()

    fun updateUser(user : User) = repo.updateUser(user)

}