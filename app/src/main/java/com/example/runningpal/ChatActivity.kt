package com.example.runningpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.db.Message
import com.example.runningpal.ui.adapters.MessageAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    companion object{

        private lateinit var messageList:MutableList<Message>

    }

    var SenderRoom : String? = null
    var ReceiverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageList =  mutableListOf<Message>()
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference()


        val name = intent.getStringExtra("name")
        val uidReceiver = intent.getStringExtra("uid")
        val uidSender = FirebaseAuth.getInstance().currentUser?.uid
        supportActionBar?.title = name


        SenderRoom = uidReceiver + uidSender
        ReceiverRoom = uidSender + uidReceiver

        val adapter = MessageAdapter(messageList)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter


        myRef.child("chats").child(SenderRoom!!).child("messages")
                .addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {

                        messageList.clear()

                        for(snap in snapshot.children){

                            val message = snap.getValue(Message::class.java)
                            messageList.add(message!!)
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }


                })


        ibChatPostMessage.setOnClickListener{

            val message = etChatPostMessage.text.toString()
            val messageObject = Message(uidSender!!,message)
            myRef.child("chats").child(SenderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    myRef.child("chats").child(ReceiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }

            addMessageToBase(messageObject,uidReceiver!!)
        }


    }

    fun addMessageToBase(message: Message, receiver : String) {

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference()


        myRef.child("TestUserChats").child(FirebaseAuth.getInstance().currentUser!!.uid).child(receiver).push()
                .setValue(message).addOnSuccessListener {

                    myRef.child("TestUserChats").child(receiver).child(FirebaseAuth.getInstance().currentUser!!.uid).push()
                            .setValue(message)

                }
    }
}