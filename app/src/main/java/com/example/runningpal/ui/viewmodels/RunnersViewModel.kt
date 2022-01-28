package com.example.runningpal.ui.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User
import com.example.runningpal.others.RunSortType
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.repositories.IRunnersRepository
import timber.log.Timber

class RunnersViewModel(val repo: IRunnersRepository) : ViewModel() {


    val user  =  repo.getCurrentUser()
    val runInvitation = repo.getRunInvitation()
    val friendInvitation = repo.getFriendInvitation()
    val runner = MutableLiveData<User>().postValue(user.value)
    val acceptedInvitation = repo.getFriendAcceptedInvitation()

    fun insertUser(user : User) = repo.insertUser(user)
    fun updateUserContacts(user: User) = repo.updateUserContacts(user)
    fun deleteAcceptedInvitation(friendID:String){
        repo.deleteAcceptedInvitation(friendID)}
    fun deleteRoomInvitation(friendID:String){ repo.deleteRoomInvitation(friendID)}

    fun deleteReceivedInvitation(friendID : String) = repo.deleteReceivedInvitation(friendID)

    fun getSelectedRunners(id: List<String>) =  repo.getSelectedRunners(id)

    fun getSelectedRunnerByNick(nick: String) =  repo.getSelectedRunnersByNick(nick)

    val allRunners = repo.getAllRunners()
    fun sendRunInvitation(invitation: Invitation) = repo.sendRunInvitation(invitation)
    fun sendFriendInvitation(friendInvitation: FriendInvitation) = repo.sendFriendInvitation(friendInvitation)
    fun updateUser(user: User) = repo.updateUser(user)
    fun getRunner(id : String) = repo.getRunner(id)
    fun acceptFriendInvitation(friendInvitation: FriendInvitation) = repo.acceptFriendInvitation(friendInvitation)
}