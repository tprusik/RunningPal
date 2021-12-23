package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User

interface IRunnersRepository {

    fun insertUser(user :User)

    fun getAllRunners() : LiveData<List<User>>

    fun getSelectedRunners( runners : List<String>) : LiveData<List<User>>

    fun getRunner(id: String) : LiveData<User>

    fun getAllRunnersSortedByLastRunDate() : LiveData<List<User>>

    fun getCurrentUser() : LiveData<User>

    fun updateUser(user : User)

    fun getRunInvitation() : LiveData<Invitation>

    fun getFriendInvitation() : LiveData<FriendInvitation>

    fun sendRunInvitation(invitation : Invitation)

    fun sendFriendInvitation(friendInvitation: FriendInvitation)

    fun updateFriendInvitationAccept(friendInvitation: FriendInvitation)

    fun deleteFriendInvitation(friendInvitation: FriendInvitation)

    suspend fun getCurrentUserObject(): User
}