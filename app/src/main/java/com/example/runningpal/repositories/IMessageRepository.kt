package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.Message
import com.example.runningpal.db.MessageContact
import com.example.runningpal.db.User

interface IMessageRepository {

    fun getMessageFriends() : LiveData<List<MessageContact>>

    fun getMessages(idReceiver : String) : LiveData<List<Message>>

    fun sendMessage(idReceiver : String,message: Message)

    fun updateMessageContact(messageContact : MessageContact, receiverContact: MessageContact)

}