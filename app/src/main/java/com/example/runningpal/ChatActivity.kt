package com.example.runningpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.fragment_message.*

class ChatActivity : AppCompatActivity() {

    companion object{

        private lateinit var messageList:MutableList<Message>

    }

    var SenderRoom : String? = null
    var ReceiverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference()



        val name = intent.getStringExtra("name")
        val uidSender = intent.getStringExtra("uid")
        val uidReceiver = FirebaseAuth.getInstance().currentUser?.uid
        supportActionBar?.title = name


        SenderRoom = uidReceiver + uidSender
        ReceiverRoom = uidSender + uidReceiver


        ibChatPostMessage.setOnClickListener{


            val message = etChatPostMessage.text.toString()

            val messageObject = Message(uidSender!!,message)

            myRef.child("chats").child(SenderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    myRef.child("chats").child(ReceiverRoom!!).child("messages").push()
                        .setValue(messageObject)

                }

        }

        //val adapter = MessageAdapter(null)
       // rvChat.adapter = adapter
        //rvChat.layoutManager = LinearLayoutManager(this)

    }
}