package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.DbConstants
import com.example.runningpal.db.DbConstants.DB_NODE_USER
import com.example.runningpal.db.Run
import com.example.runningpal.db.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class RunnersRepository : IRunnersRepository {

    override fun getAllRunners(): LiveData<List<User>> {

            var  runners  = MutableLiveData<List<User>>()

            val uid = FirebaseAuth.getInstance().currentUser!!.uid

            val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

            database.getReference(DbConstants.DB_NODE_USER)
                    .addValueEventListener(object : ValueEventListener {

                        val users =  mutableListOf<User>()

                        override fun onDataChange(snapshot: DataSnapshot) {

                            for(snap in snapshot.children){

                                val user = snap.getValue(User::class.java)

                                if(user!!.uid!=uid)
                                users.add(user!!)
                            }

                            runners.postValue(users)

                        }

                        override fun onCancelled(error: DatabaseError) {}

                    })

            return runners


    }

    override fun getRunner(id: String): LiveData<User> {

            var  user  = MutableLiveData<User>()

            val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

            database.getReference(DB_NODE_USER).child(id)
                .addValueEventListener(object : ValueEventListener {



                    override fun onDataChange(snapshot: DataSnapshot) {

                        val us = snapshot.getValue(User::class.java)!!

                        Timber.d("ss"+ us.email)
                        user.postValue(us!!)

                    }



                    override fun onCancelled(error: DatabaseError) {}

                })

            return user

        }

    override fun getAllRunnersSortedByLastRunDate(): LiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): LiveData<User> {

        var  user  = MutableLiveData<User>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

        database.getReference(DB_NODE_USER)
                .addValueEventListener(object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val currentUser = snap.getValue(User::class.java)
                            user.postValue(currentUser!!)

                        }


                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return user

    }



    override fun updateUser(user: User) {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL).getReference(DB_NODE_USER).child(uid)

        database.setValue(user)

    }
}