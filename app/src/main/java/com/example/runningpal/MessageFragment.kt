package com.example.runningpal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.*
import kotlin.collections.ArrayList


class MessageFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val chatMates =  getMessages()

        val adapter = ContactsAdapter()
        rvMessageFragment.adapter = adapter
        rvMessageFragment.layoutManager = LinearLayoutManager(context)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    fun getMessages() : ArrayList<String>{

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference()

        val chatMates = ArrayList<String>()

        myRef.child("TestUserChats").child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(snap in snapshot.children){

                    chatMates.add(snap.getValue().toString())

                }


            }

            override fun onCancelled(error: DatabaseError) {

            }


        })

        return chatMates
    }


}
