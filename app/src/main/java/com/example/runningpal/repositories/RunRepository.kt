package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.*
import com.example.runningpal.db.DbConstants.DB_INSTANCE_URL
import com.example.runningpal.db.DbConstants.DB_NODE_RUN
import com.example.runningpal.db.DbConstants.DB_NODE_RUN_ROOM
import com.example.runningpal.db.DbConstants.ORDER_BY_DATE
import com.example.runningpal.db.DbConstants.ORDER_BY_DISTANCE
import com.example.runningpal.db.DbConstants.ORDER_BY_TIME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import timber.log.Timber

class RunRepository : IRunRepository  {


    override fun insertRun(run: Run) {

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)
        val myRef = database.getReference(DB_NODE_RUN).child(uid).push().setValue(run)

    }

    override fun deleteRun(run: Run) {}

    override fun getAllRunsSortedByDate() : LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DATE) }

    override fun getAllRunsSortedByDistance(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DISTANCE) }

    override fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_TIME) }

    override fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DISTANCE) }


    override fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("caloriesBurnt")
                .addValueEventListener(object : ValueEventListener {

                    val listr =  mutableListOf<Run>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val run = snap.getValue(Run::class.java)!!
                            listr.add(run)
                            Timber.d(run.avgSpeedKmh.toString())

                            runs.postValue(listr)

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return runs

    }

    override fun getTotalAvgSpeed() {}

    override fun getTotalCaloriesBurned() {}

    override fun getTotalTimeinMillis() {}

    override fun getTotalDistance()  {}

    override fun getTotalStatistics(): LiveData<RunStatistics> {

        var totalStatistics = MutableLiveData<RunStatistics>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)
        val myRef = database.getReference(DB_NODE_RUN).child(uid)
            .addValueEventListener(object : ValueEventListener {

                var totRuns = 0
                var totDistance = 0
                var totCaloriesBurned = 0
                var totTime = 0L

                override fun onDataChange(snapshot: DataSnapshot) {

                    for(snap in snapshot.children){

                        val run = snap.getValue(Run::class.java)!!

                        totRuns ++
                        totDistance += run.distanceMetres
                        totCaloriesBurned += run.caloriesBurnt
                        totTime += run.timeInMilis


                    }

                    var totStats = RunStatistics(totRuns,totDistance,totCaloriesBurned,totTime)

                    totalStatistics.postValue(totStats)

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return totalStatistics

    }

    fun getDatabaseReference(orderByPath : String) : LiveData<List<Run>>{

        var  sortedRuns  = MutableLiveData<List<Run>>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)

        database.getReference(DB_NODE_RUN).child(uid).orderByChild(orderByPath)
                .addValueEventListener(object : ValueEventListener {

                    val runs =  mutableListOf<Run>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            runs.add(snap.getValue(Run::class.java)!!)
                        }

                        sortedRuns.postValue(runs)

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return sortedRuns

    }

    override  fun addRunnerToRoom(runner : Runner){

        Timber.d("add Runner ${runner.name}  ${runner.idRoom}  ${runner.id}")
        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)

        val myRef = database.getReference(DB_NODE_RUN_ROOM).child(runner.idRoom!!).child("RUNNER").child(runner.id!!).setValue(runner)
        Timber.d("add Runner ${runner.name}  ${runner.idRoom}")

    }

    override fun getRoom(roomID: String): LiveData<Room> {

        var  room  = MutableLiveData<Room>()

        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)

        database.getReference(DB_NODE_RUN_ROOM).child(roomID)
                .addValueEventListener(object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {

                        val roomObject = snapshot.getValue(Room::class.java)

                        if(roomObject!=null) {

                            Timber.d("room z firebase ${roomObject.id} ")

                            room.postValue(roomObject!!)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return room

    }

    override fun getRunners(roomID: String): LiveData<List<Runner>> {

        var  room = MutableLiveData<List<Runner>>()
        var  runnerList = mutableListOf<Runner>()

        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)

        database.getReference(DB_NODE_RUN_ROOM).child(roomID).child("RUNNER")
                .addValueEventListener(object : ValueEventListener {


                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val runnerObject = snap.getValue(Runner::class.java)

                            if(runnerObject!=null) {

                                Timber.d("runner z firebase ${runnerObject.id} ")
                                runnerList.add(runnerObject)

                            }


                        }

                        room.postValue(runnerList!!)

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return room

    }

    override fun createRoom(room : Room){

        val database = FirebaseDatabase.getInstance(DB_INSTANCE_URL)

        val myRef = database.getReference(DB_NODE_RUN_ROOM).child(room.id!!).setValue(room)
        Timber.d("add Room ${room.id} ")
    }

}