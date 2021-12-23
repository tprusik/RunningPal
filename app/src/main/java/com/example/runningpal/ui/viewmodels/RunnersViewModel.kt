package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User
import com.example.runningpal.others.RunSortType
import com.example.runningpal.repositories.IRunnersRepository

class RunnersViewModel(val repo: IRunnersRepository) : ViewModel() {

    val user  =  repo.getCurrentUser()
    val runInvitation = repo.getRunInvitation()
    val friendInvitation = repo.getFriendInvitation()
    val runner = MutableLiveData<User>().postValue(user.value)

    fun insertUser(user : User) = repo.insertUser(user)
    fun getSelectedRunners(id: List<String>) =  repo.getSelectedRunners(id)
    val allRunners = repo.getAllRunners()
    fun sendRunInvitation(invitation: Invitation) = repo.sendRunInvitation(invitation)
    fun sendFriendInvitation(friendInvitation: FriendInvitation) = repo.sendFriendInvitation(friendInvitation)
    fun updateUser(user: User) = repo.updateUser(user)

}