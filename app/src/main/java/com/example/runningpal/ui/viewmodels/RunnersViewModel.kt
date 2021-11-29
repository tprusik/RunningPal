package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningpal.db.User
import com.example.runningpal.others.RunSortType
import com.example.runningpal.repositories.IRunnersRepository

class RunnersViewModel(val repo: IRunnersRepository) : ViewModel() {

    val runner = MutableLiveData<User>()

    val runners = MediatorLiveData<User>()

    val selectedItem: LiveData<User> get() = runner

    val user  =  repo.getCurrentUser()

    fun selectItem(item: User) {
        runner.value = item
    }

    init {
        runners.addSource(user) { result ->

            result?.let { runners.value = it }

        }
    }

    fun getSelectedRunners(id: List<String>) =  repo.getSelectedRunners(id)

    fun retRunner() : LiveData<User> { return runner}

    fun getRunner(id: String) = repo.getRunner(id)

    val allRunners = repo.getAllRunners()


    fun updateUser(user: User) = repo.updateUser(user)

}