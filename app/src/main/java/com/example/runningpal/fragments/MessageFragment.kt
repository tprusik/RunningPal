package com.example.runningpal.fragments

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.DatabaseUtility
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.adapters.MessageFriendAdapter
import com.example.runningpal.ui.adapters.NewMessageAdapter
import com.example.runningpal.ui.viewmodels.MessageViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_message.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get
import timber.log.Timber
import kotlin.collections.ArrayList


class MessageFragment : Fragment() {

    private lateinit var contactsAdapter: NewMessageAdapter
    private lateinit var messageAdapter: MessageFriendAdapter
    private lateinit var messageViewModel: MessageViewModel
    private lateinit var userViewModel: RunnersViewModel
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycleView()
        messageViewModel = get()
        userViewModel = get()
        user = User()


        messageViewModel.messageFriends.observe(viewLifecycleOwner, Observer {

            Timber.d("Message Fragment")
            messageAdapter.submitList(it)


        })


        userViewModel.user.observe(viewLifecycleOwner, Observer {

           user = it

        })





        fabMessageFragment.setOnClickListener{

            userViewModel.getSelectedRunner(user.contacts!!).observe(viewLifecycleOwner, Observer {

                contactsAdapter.submitList(it)

            })

            changeRecycleView()

        }


    }


    private fun setupRecycleView() = rvMessageFragment.apply{

        contactsAdapter = NewMessageAdapter()
        messageAdapter =  MessageFriendAdapter()
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(context)

    }

    private fun changeRecycleView() = rvMessageFragment.apply{

        if(adapter is MessageFriendAdapter){
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(context)

        }else {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(context)

        }



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_message, container, false)
    }


}
