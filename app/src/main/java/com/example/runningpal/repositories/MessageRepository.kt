package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.DbConstants
import com.example.runningpal.db.Message
import com.example.runningpal.db.MessageContact
import com.example.runningpal.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class MessageRepository : IMessageRepository {

    override fun getMessageFriends(): LiveData<List<MessageContact>> {

            val messageContactsLiveData = MutableLiveData<List<MessageContact>>()

            val messageContacts = mutableListOf<MessageContact>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

                database.getReference(DbConstants.DB_NODE_MESSAGE_FRIENDS).child(uid).child("MESSAGE_CONTACTS")
                        .addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {

                                for(snap in snapshot.children) {

                                    val messageContact = snap.getValue(MessageContact::class.java)

                                    if (messageContact != null) {
                                        Timber.d("MessageRepository getMessageFriends  " + messageContact.name)

                                        messageContacts.add(messageContact)
                                    }
                                }

                                messageContactsLiveData.postValue(messageContacts)

                            }

                            override fun onCancelled(error: DatabaseError) {}

                        })



            return messageContactsLiveData

        }

    override fun getMessages(idReceiver: String): LiveData<List<Message>> {

        val messageLiveData = MutableLiveData<List<Message>>()

        val messages = mutableListOf<Message>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)


        database.getReference(DbConstants.DB_NODE_MESSAGE_FRIENDS).child(uid).child(idReceiver)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children) {

                            val message = snap.getValue(Message::class.java)

                            if (message != null) {
                                Timber.d("tim" + message.message + " ss ")
                                messages.add(message)
                            }
                        }


                        messageLiveData.postValue(messages)

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return messageLiveData

    }

    override fun sendMessage(idReceiver: String,message : Message) {

        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)
        val myRef = database.getReference()


        myRef.child("TestUserChats").child(FirebaseAuth.getInstance().currentUser!!.uid).child(idReceiver).push()
                .setValue(message).addOnSuccessListener {

                    myRef.child("TestUserChats").child(idReceiver).child(FirebaseAuth.getInstance().currentUser!!.uid).push()
                            .setValue(message)

                }

    }

    override fun updateMessageContact(messageContact : MessageContact) {

        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)
        val myRef = database.getReference()


        myRef.child("TestUserChats").child(FirebaseAuth.getInstance().currentUser!!.uid).child("MESSAGE_CONTACTS").child(messageContact.uid!!)
                .setValue(messageContact).addOnSuccessListener {

                    myRef.child("TestUserChats").child(messageContact.uid!!).child("MESSAGE_CONTACTS").child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(messageContact)
                }

    }
}