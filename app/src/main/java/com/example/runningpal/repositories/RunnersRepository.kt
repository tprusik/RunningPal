package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.others.DbConstants
import com.example.runningpal.others.DbConstants.DB_NODE_RUN_INVITATION
import com.example.runningpal.others.DbConstants.DB_NODE_USER
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User
import com.example.runningpal.others.DbConstants.DB_NODE_FRIEND_INVITATION
import com.example.runningpal.others.DbConstants.DB_NODE_INVITATION
import com.example.runningpal.others.DbConstants.DB_NODE_USER_CONTACTS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import timber.log.Timber

class RunnersRepository : IRunnersRepository {

    private  var userID : String
    private var database : FirebaseDatabase

    init {
        userID = FirebaseAuth.getInstance().currentUser!!.uid
        Timber.d("Timber repo "+ userID)
        database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL) }

    override fun insertUser(user :User){database.getReference(DB_NODE_USER).child(user.uid!!).setValue(user)}
    override fun updateUser(user: User) { database.getReference(DB_NODE_USER).child(userID).setValue(user) }
    override fun updateUserContacts(user: User) { database.getReference(DB_NODE_USER).child(userID).child(DB_NODE_USER_CONTACTS).setValue(user.contacts) }
    override fun sendRunInvitation(invitation: Invitation) {database.getReference(DB_NODE_INVITATION).child(DB_NODE_RUN_INVITATION).child(invitation.receiverID!!).child(userID).setValue(invitation) }

    override fun sendFriendInvitation(friendInvitation: FriendInvitation) {
        database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(friendInvitation.receiverID!!).child(friendInvitation.senderID!!).setValue(friendInvitation) }

    override fun deleteAcceptedInvitation(friendID : String){ database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(userID).child(friendID).removeValue() }

    override fun deleteRoomInvitation(friendID : String){ database.getReference(DB_NODE_INVITATION).child(DB_NODE_RUN_INVITATION).child(userID).child(friendID).removeValue() }

    override fun deleteReceivedInvitation(friendID : String){ database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(userID).child("ACCEPTED").child(friendID).removeValue() }

    override fun getFriendAcceptedInvitation(): LiveData<String> {

        var invitation = MutableLiveData<String>()

        database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(userID).child("ACCEPTED")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            Timber.d("invitatiom")
                            val currInvitation = snap.getValue().toString()
                            invitation.postValue(currInvitation!!)

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}

                })

        return invitation
    }

    override fun acceptFriendInvitation(friendInvitation: FriendInvitation) {
        database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(friendInvitation.senderID!!).child("ACCEPTED").child(friendInvitation.receiverID!!).setValue(friendInvitation.receiverID)
    }

    override fun deleteFriendInvitation(friendInvitation: FriendInvitation) {
        database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(friendInvitation.receiverID!!).child(friendInvitation.senderID!!).removeValue()
                .addOnSuccessListener {
                    database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(friendInvitation.senderID).child(friendInvitation.receiverID).removeValue()
                }
    }



    override fun getAllRunners(): LiveData<List<User>> {

            var  runners  = MutableLiveData<List<User>>()

            database.getReference(DB_NODE_USER)
                    .addValueEventListener(object : ValueEventListener {
                        val users =  mutableListOf<User>()
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for(snap in snapshot.children){

                                val user = snap.getValue(User::class.java)

                                if(user!!.uid!=userID)
                                users.add(user!!)
                            }

                            runners.postValue(users)

                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

            return runners

    }


    override fun getSelectedRunnersByNick(nick: String): LiveData<List<User>> {

        val selectedRunners = MutableLiveData<List<User>>()
        val runners = mutableListOf<User>()


        database.getReference(DB_NODE_USER)
                .addValueEventListener(object : ValueEventListener {
                    val users =  mutableListOf<User>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val user = snap.getValue(User::class.java)

                            if(user!!.nick!=nick)
                                users.add(user!!)
                        }

                        selectedRunners.postValue(users)

                    }
                    override fun onCancelled(error: DatabaseError) {}
                })



        return selectedRunners

    }

    override fun getSelectedRunners(ids: List<String>): LiveData<List<User>> {

       val selectedRunners = MutableLiveData<List<User>>()
        val runners = mutableListOf<User>()

        for (id in ids){
            database.getReference(DB_NODE_USER).child(id)
                    .addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            val us = snapshot.getValue(User::class.java)
                            if(us!=null){

                                Timber.d("ss"+ us.email)

                                runners.add(us)

                                selectedRunners.postValue(runners)
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {}

                    })

        }

        return selectedRunners

    }

    override fun getRunner(id: String): LiveData<User> {

            var  user  = MutableLiveData<User>()

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

        database.getReference(DB_NODE_USER)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val currentUser = snap.getValue(User::class.java)

                            if(currentUser!!.uid==userID)
                            user.postValue(currentUser!!)

                        }

                    }


                    override fun onCancelled(error: DatabaseError) {}
                })

        return user
    }

    override suspend fun getCurrentUserObject(): User{

        var user = User()

            database.getReference(DB_NODE_USER)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snap in snapshot.children) {

                                var currentUser = snap.getValue(User::class.java)

                                if (currentUser!!.uid == userID) {
                                    user= currentUser
                                    Timber.d("users " + user.nick)
                                }
                            }

                        }

                        override fun onCancelled(error: DatabaseError) {}

                    })

                Timber.d("user " + user.nick)

             return user
    }




    override fun getRunInvitation(): LiveData<Invitation> {

        var invitation = MutableLiveData<Invitation>()

        database.getReference(DB_NODE_INVITATION).child(DB_NODE_RUN_INVITATION).child(userID)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            Timber.d("invitatiom")
                            val currInvitation = snap.getValue(Invitation::class.java)
                            invitation.postValue(currInvitation!!)

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}

                })

        return invitation
    }

    override fun getFriendInvitation(): LiveData<FriendInvitation> {

        var invitation = MutableLiveData<FriendInvitation>()

        database.getReference(DB_NODE_INVITATION).child(DB_NODE_FRIEND_INVITATION).child(userID)
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            Timber.d("invitatiom")
                            val currInvitation = snap.getValue(FriendInvitation::class.java)
                            invitation.postValue(currInvitation!!)

                        }
                    }
                    override fun onCancelled(error: DatabaseError) {}

                })

        return invitation

    }


}