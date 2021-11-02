package com.example.runningpal.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.ContactsAdapter
import com.example.runningpal.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_contacts.*
import org.koin.android.ext.android.get
import timber.log.Timber


class ContactsFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val myViewModel: MainViewModel =  get()
        val usera  : FirebaseAuth = get()



        Timber.tag("TagNameThatIsReallyReallyReallyLong").d(myViewModel.repo.testFun());

        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference().child("UserData")


        var contacts =  mutableListOf<User>()

        val adapter = ContactsAdapter(contacts)
        rvContacts.adapter = adapter
        rvContacts.layoutManager = LinearLayoutManager(context)

        btnContactsFindNew.setOnClickListener{

            val intent  = Intent(context, FindContactActivity::class.java)
            startActivity(intent)

        }

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


             for(postSnapshot in snapshot.children)
             {

                 val user  = postSnapshot.getValue(User::class.java)
                 contacts.add(user!!)


             }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

}