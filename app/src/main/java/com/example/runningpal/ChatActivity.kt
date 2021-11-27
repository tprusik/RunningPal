package com.example.runningpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningpal.db.Message
import com.example.runningpal.others.DatabaseUtility
import com.example.runningpal.others.DatabaseUtility.convertStringToBitmap
import com.example.runningpal.ui.adapters.MessageAdapter
import com.example.runningpal.ui.viewmodels.MessageViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.user_item.view.*
import org.koin.android.ext.android.get
import timber.log.Timber

class ChatActivity : AppCompatActivity() {


    private lateinit var messageViewModel : MessageViewModel
    private lateinit var messageAdapter : MessageAdapter


    var SenderRoom : String? = null
    var ReceiverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        messageViewModel = get()
        setupRecyclerView()

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference()


        val pic = intent.getStringExtra("pic")
       // val idReceiver = intent.getStringExtra("id")
       val idReceiver =  "oVuVAUfn0JO9MfSbZuwFcuWuYsl2"
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


       // SenderRoom = uidReceiver + uidSender
      //  ReceiverRoom = uidSender + uidReceiver


        messageViewModel.getMessages(idReceiver!!).observe(this, Observer {

            Timber.d("jestem tutaj ? ")
            messageAdapter.submitList(it)

        })


        ibChatPostMessage.setOnClickListener{

            val message = etChatPostMessage.text.toString()
            val messageObject = Message(uidSender!!,message)


           messageViewModel.sendMessage(idReceiver!!,messageObject)
        }


    }

    fun setupRecyclerView() = rvChat.apply {

        messageAdapter = MessageAdapter()
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(context)

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