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
    fun addRunnerToRoom(runner : Runner)
    fun getRoom(roomID : String) : LiveData<Room>
    fun getRunners(roomID: String): LiveData<List<Runner>>

    fun getAllRunsSortedByDate() : LiveData<List<Run>>
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>
    fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>>
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    fun getTotalStatistics() : LiveData<RunStatistics>

    // czy one są konieczne ?

    fun getTotalAvgSpeed()
    fun getTotalCaloriesBurned()
    fun getTotalTimeinMillis()
    fun getTotalDistance()


}