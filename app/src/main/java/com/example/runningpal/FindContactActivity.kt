package com.example.runningpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.db.User
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_find_contact.*

class FindContactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_contact)


        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference().child("UserData")

        var users =  mutableListOf<User>()

        val adapter = ContactsAdapter(users)
        rvFindContact.adapter = adapter
        rvFindContact.layoutManager = LinearLayoutManager(this)


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for(postSnapshot in snapshot.children)
                {

                    val user  = postSnapshot.getValue(User::class.java)
                    users.add(user!!)


                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



    }
}