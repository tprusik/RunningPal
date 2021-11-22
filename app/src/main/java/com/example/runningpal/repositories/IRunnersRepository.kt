package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.User

interface IRunnersRepository {

    fun getAllRunners() : LiveData<List<User>>

    fun getAllRunnersSortedByLastRunDate() : LiveData<List<User>>

    fun getCurrentUser() : LiveData<User>

    fun updateUser(user : User)

}