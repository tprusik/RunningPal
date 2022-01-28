package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.*
import com.example.runningpal.others.DbConstants.DB_INSTANCE_URL
import com.example.runningpal.others.DbConstants.DB_NODE_RUN
import com.example.runningpal.others.DbConstants.DB_NODE_RUN_ROOM
import com.example.runningpal.others.DbConstants.DB_NODE_RUN_ROOM_HISTORY
import com.example.runningpal.others.DbConstants.ORDER_BY_CALORIES_BURNED
import com.example.runningpal.others.DbConstants.ORDER_BY_DATE
import com.example.runningpal.others.DbConstants.ORDER_BY_DISTANCE
import com.example.runningpal.others.DbConstants.ORDER_BY_TIME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class RunRepository : IRunRepository  {


    private var userID : String
    private var database : FirebaseDatabase

    init{
        userID =  FirebaseAuth.getInstance().currentUser!!.uid
        database =  FirebaseDatabase.getInstance(DB_INSTANCE_URL)
    }

    override fun insertRun(run: Run) { database.getReference(DB_NODE_RUN).child(userID).push().setValue(run) }
    override fun deleteRun(run: Run) {}
    override fun getAllRunsSortedByDate() : LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DATE) }
    override fun getAllRunsSortedByDistance(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DISTANCE) }
    override fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_TIME) }
    override fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_DISTANCE) }
    override fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>> { return getDatabaseReference(ORDER_BY_CALORIES_BURNED)}


    override fun getTotalStatistics(userID : String): LiveData<RunStatistics> {
        var totalStatistics = MutableLiveData<RunStatistics>()

       database.getReference(DB_NODE_RUN).child(userID)
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
                        totCaloriesBurned += run.caloriesBurned
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

        database.getReference(DB_NODE_RUN).child(userID).orderByChild(orderByPath)
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
        Timber.d("add Runner ${runner.name}  ${runner.idRoom}  ${runner.distanceMetres}")
        database.getReference(DB_NODE_RUN_ROOM).child(runner.idRoom!!).child("RUNNER").child(runner.id!!).setValue(runner)

    }

    override fun getRoom(roomID: String): LiveData<Room> {
        var  room  = MutableLiveData<Room>()



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

        database.getReference(DB_NODE_RUN_ROOM).child(roomID).child("RUNNER").orderByChild("distanceMetres")
                .addValueEventListener(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {

                        var  runnerList = mutableListOf<Runner>()

                        for(snap in snapshot.children){

                            val runnerObject = snap.getValue(Runner::class.java)

                            if(runnerObject!=null) {

                                runnerList.add(runnerObject)
                                Timber.d("runner z firebase ${runnerList.size} ")

                            }

                        }

                        room.postValue(runnerList!!)

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return room

    }

    override fun getRoomState(roomID: String): LiveData<Room> {

        var  room = MutableLiveData<Room>()

        database.getReference(DB_NODE_RUN_ROOM).child(roomID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {


                        val roomObj = snapshot.getValue(Room::class.java)

                        if(roomObj!=null) {
                            room.postValue(roomObj!!)
                           // Timber.d("RoomState z firebase ${isStarted} ")
                        }

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return room

    }


    override fun createRoom(room : Room){ database.getReference(DB_NODE_RUN_ROOM).child(room.id!!).setValue(room)
        Timber.d("add Room ${room.id} ") }


    override fun updateRoomState(room : Room){ database.getReference(DB_NODE_RUN_ROOM).child(room.id!!).child("started").setValue(true)
        Timber.d("add Room ${room.id} ") }

    override fun deleteRoom(room: Room) { database.getReference(DB_NODE_RUN_ROOM).child(room.id!!).removeValue() }

    override fun insertRoomHistory(roomHistory: RoomHistory) {database.getReference(DB_NODE_RUN_ROOM_HISTORY).child(userID).push().setValue(roomHistory)}


    override fun updateRoomTime(time : Int,roomID : String){ database.getReference(DB_NODE_RUN_ROOM).child(roomID).child("timeToEnd").setValue(time) }

    override fun getRoomHistory(): LiveData<List<RoomHistory>> {

        var  roomHistory  = MutableLiveData<List<RoomHistory>>()

        database.getReference(DB_NODE_RUN_ROOM_HISTORY).child(userID)

                .addValueEventListener(object : ValueEventListener {

                    val rooms =  mutableListOf<RoomHistory>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            rooms.add(snap.getValue(RoomHistory::class.java)!!)
                        }

                        roomHistory.postValue(rooms)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

        return roomHistory

    }


}