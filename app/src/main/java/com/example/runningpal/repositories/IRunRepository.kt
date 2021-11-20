package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.Run
import com.example.runningpal.db.RunStatistics

interface IRunRepository {

    fun insertRun(run : Run)
    fun deleteRun(run : Run)


    fun getAllRunsSortedByDate() : LiveData<List<Run>>
    fun getAllRunsSortedByDistance(): LiveData<List<Run>>
    fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>>
    fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>>
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>

    fun getTotalAvgSpeed()
    fun getTotalCaloriesBurned()
    fun getTotalTimeinMillis()
    fun getTotalDistance()

    fun getTotalStatistics() : LiveData<RunStatistics>

}