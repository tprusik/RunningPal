package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User

interface IRunnersRepository {

    fun insertUser(user :User)

    fun getAllRunners() : LiveData<List<User>>

    fun updateUserContacts(user: User)

    fun getSelectedRunners( runners : List<String>) : LiveData<List<User>>

    fun getRunner(id: String) : LiveData<User>

    fun deleteAcceptedInvitation(friendID : String)

    fun getAllRunnersSortedByLastRunDate() : LiveData<List<User>>

    fun getCurrentUser() : LiveData<User>

    fun updateUser(user : User)

    fun getSelectedRunnersByNick(nick: String): LiveData<List<User>>

    fun getRunInvitation() : LiveData<Invitation>

    fun getFriendInvitation() : LiveData<FriendInvitation>

    fun getFriendAcceptedInvitation() : LiveData<String>

    fun sendRunInvitation(invitation : Invitation)

    fun sendFriendInvitation(friendInvitation: FriendInvitation)

    fun acceptFriendInvitation(friendInvitation: FriendInvitation)

    fun deleteReceivedInvitation(friendID : String)
    fun deleteFriendInvitation(friendInvitation: FriendInvitation)
    fun deleteRoomInvitation(friendID : String)
    suspend fun getCurrentUserObject(): User
}