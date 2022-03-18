package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.*

interface IRunRepository {




    fun insertRun(run : Run)
    fun deleteRun(run : Run)

    fun createRoom(room: Room)
    fun updateRoomState(room : Room)
    fun deleteRoom(room : Room)
    fun insertRoomHistory(roomHistory : RoomHistory)
    fun addRunnerToRoom(runner : Runner)
    fun getRoom(roomID : String) : LiveData<Room>
    fun getRunners(roomID: String): LiveData<List<Runner>>
    fun getRoomState(roomID: String): LiveData<Room>


    fun getAllRunsSortedByDate() : LiveData<List<Run>>
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>
    fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>>
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    fun getTotalStatistics(userID : String) : LiveData<RunStatistics>

    fun updateRoomTime(time : Int,id : String)
    fun getRoomHistory(): LiveData<List<RoomHistory>>

    // czy one są konieczne ?


}