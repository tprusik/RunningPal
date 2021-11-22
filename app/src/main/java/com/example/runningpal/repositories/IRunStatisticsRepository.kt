package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import com.example.runningpal.db.Run
import com.example.runningpal.db.RunStatistics

interface IRunStatisticsRepository {

    fun insertRunStatistics(runStatistics : RunStatistics)

    fun getRunStatistics(userID : String): LiveData<List<RunStatistics>>

}