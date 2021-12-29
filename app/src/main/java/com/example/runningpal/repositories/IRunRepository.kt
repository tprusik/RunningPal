package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.Room
import com.example.runningpal.db.Run
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.db.Runner

interface IRunRepository {

    fun insertRun(run : Run)
    fun deleteRun(run : Run)

    fun createRoom(room: Room)
    fun updateRoomState(room : Room)
    fun deleteRoom(room : Room)
    fun addRunnerToRoom(runner : Runner)
    fun getRoom(roomID : String) : LiveData<Room>
    fun getRunners(roomID: String): LiveData<List<Runner>>
    fun getRoomState(roomID: String): LiveData<Room>


    fun getAllRunsSortedByDate() : LiveData<List<Run>>
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>
    fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>>
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    fun getTotalStatistics() : LiveData<RunStatistics>

    fun updateRoomTime(time : Int,id : String)

    // czy one są konieczne ?

    fun getTotalAvgSpeed()
    fun getTotalCaloriesBurned()
    fun getTotalTimeinMillis()
    fun getTotalDistance()

}