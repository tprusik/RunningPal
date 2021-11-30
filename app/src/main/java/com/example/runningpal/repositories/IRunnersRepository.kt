package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User

interface IRunnersRepository {

    fun getAllRunners() : LiveData<List<User>>

    fun getSelectedRunners( runners : List<String>) : LiveData<List<User>>

    fun getRunner(id: String) : LiveData<User>

    fun getAllRunnersSortedByLastRunDate() : LiveData<List<User>>

    fun getCurrentUser() : LiveData<User>

    fun updateUser(user : User)

    fun getInvitation() : LiveData<Invitation>

    fun sendInvitation(invitation : Invitation)

}