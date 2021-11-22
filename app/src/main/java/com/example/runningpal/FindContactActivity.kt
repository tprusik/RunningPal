package com.example.runningpal

import android.content.ContentProvider
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.db.User
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_find_contact.*
import kotlinx.android.synthetic.main.fragment_run.*
import org.koin.android.ext.android.get

class FindContactActivity : AppCompatActivity() {

    private lateinit var viewModel : RunnersViewModel
    private  lateinit var userAdapter : ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_contact)

        viewModel = get()
        setupRecyclerView()


        viewModel.allRunners.observe(this, Observer {
            userAdapter.submitList(it)
        })


    }

    private fun setupRecyclerView() = rvFindContact.apply {
        userAdapter = ContactsAdapter()
        adapter = userAdapter
        layoutManager = LinearLayoutManager(context)
    }

}