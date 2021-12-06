package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.others.DbConstants
import com.example.runningpal.db.RunStatistics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RunStatisticsRepository : IRunStatisticsRepository {

    override fun insertRunStatistics(runStatistics: RunStatistics) {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)
        database.getReference(DbConstants.DB_NODE_RUN_STATISTICS).child(uid).push().setValue(runStatistics)

    }

    override fun getRunStatistics(userID: String): LiveData<List<RunStatistics>> {

        var  sortedRuns  = MutableLiveData<List<RunStatistics>>()
        val uid = userID
        val database = FirebaseDatabase.getInstance(DbConstants.DB_INSTANCE_URL)

        database.getReference(DbConstants.DB_NODE_RUN).child(uid)
                .addValueEventListener(object : ValueEventListener {

                    val runStatistics =  mutableListOf<RunStatistics>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            runStatistics.add(snap.getValue(RunStatistics::class.java)!!)
                        }

                        sortedRuns.postValue(runStatistics)

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return sortedRuns

    }

}

