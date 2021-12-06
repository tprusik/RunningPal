package com.example.runningpal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.Message
import com.example.runningpal.db.MessageContact
import com.example.runningpal.others.DatabaseUtility.convertStringToBitmap
import com.example.runningpal.ui.adapters.MessageAdapter
import com.example.runningpal.ui.viewmodels.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat.*
import org.koin.android.ext.android.get
import timber.log.Timber
import java.util.*

class ChatActivity : AppCompatActivity() {

    private lateinit var messageViewModel : MessageViewModel
    private lateinit var messageAdapter : MessageAdapter
    private lateinit var receiverID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageViewModel = get()
        setupRecyclerView()


        val pic = intent.getStringExtra("pic")
        receiverID = intent.getStringExtra("id")!!
        val nameReceiver = intent.getStringExtra("name")

        tvChatAactivity.setText(nameReceiver)

        if(pic==null){

            Glide.with(this).load(R.drawable.default_user_avatar).into(ivChatActivity)

        }
        else{

            val image =  convertStringToBitmap(pic)
            Glide.with(this).load(image).into(ivChatActivity)
        }

        val uidSender = FirebaseAuth.getInstance().currentUser?.uid


         getMessages()


        ibChatPostMessage.setOnClickListener{
            val timestamp = System.currentTimeMillis().toString()

            val message = etChatPostMessage.text.toString()
            val messageObject = Message(uidSender!!,message,UUID.randomUUID().toString())
            val messageContact = MessageContact(nameReceiver,message,timestamp,receiverID,pic)

            messageViewModel.sendMessage(receiverID,messageObject)
            messageViewModel.updateMessageContact(messageContact)

           getMessages()

        }


    }

    fun getMessages(){

        messageViewModel.getMessages(receiverID).observe(this, Observer {

            Timber.d("jestem tutaj ? ")
            messageAdapter.submitList(it)

        })


    }

    override fun onStop() {
        super.onStop()

        messageViewModel.getMessages(receiverID).observe(this, Observer {

            Timber.d("jestem tutaj ? ")
            messageAdapter.submitList(it)

        })

    }


    fun setupRecyclerView() = rvChat.apply {

        messageAdapter = MessageAdapter()
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(context)

    }

}