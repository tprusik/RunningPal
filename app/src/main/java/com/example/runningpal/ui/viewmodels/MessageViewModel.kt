package com.example.runningpal.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.runningpal.db.Message
import com.example.runningpal.db.MessageContact
import com.example.runningpal.repositories.IMessageRepository
import com.example.runningpal.repositories.IRunnersRepository

class MessageViewModel(val repository : IMessageRepository) : ViewModel() {

    val messageFriends = repository.getMessageFriends()

    fun getMessages(idReceiver : String)  = repository.getMessages(idReceiver)

    fun sendMessage(idReceiver : String,message: Message) = repository.sendMessage(idReceiver,message)

    fun updateMessageContact(messageContact: MessageContact) = repository.updateMessageContact(messageContact)


}