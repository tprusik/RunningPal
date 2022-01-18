package com.example.runningpal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.Message
import com.example.runningpal.db.MessageContact
import com.example.runningpal.others.Utils.convertStringToBitmap
import com.example.runningpal.others.Utils.getUserSharedPrevs
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
    private lateinit var nameReceiver : String
    private lateinit var pic : String

    companion object{ val contact = MutableLiveData<MessageContact>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageViewModel = get()
        setupRecyclerView()

        val user = getUserSharedPrevs(applicationContext)
        Timber.d("Shared " + user.nick)

        contact.observe(this, Observer {
            tvChatActivity.setText(it.name)

            if(it.profilePic==null){
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivChatActivity) }
            else {
                pic = it.profilePic
                val image =  convertStringToBitmap(it.profilePic)
                Glide.with(this).load(image).into(ivChatActivity) }

            it.uid?.let { receiverID = it }
            nameReceiver = it.name!!

            getMessages()
        })



        ibChatPostMessage.setOnClickListener{


            val timestamp = System.currentTimeMillis().toString()
            val message = etChatPostMessage.text.toString()
            val messageObject = Message(user.uid,message,UUID.randomUUID().toString())

            val senderContact = MessageContact(nameReceiver,message,timestamp,receiverID,null)
            val receiverContact = MessageContact(user.nick,message,timestamp,user.uid,user.profilePic)

            messageViewModel.sendMessage(receiverID,messageObject)
            messageViewModel.updateMessageContact(senderContact,receiverContact)

           getMessages()

            etChatPostMessage.text.clear()
        }
    }

    fun getMessages(){

        messageViewModel.getMessages(receiverID).observe(this, Observer {
            messageAdapter.submitList(it)

           rvChat.scrollToPosition(it.size - 1)

        })



    }



    fun setupRecyclerView() = rvChat.apply {

        messageAdapter = MessageAdapter()
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(context)

    }

}