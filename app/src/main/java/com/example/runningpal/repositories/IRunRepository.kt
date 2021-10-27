package com.example.runningpal.repositories

import com.example.runningpal.db.Run

interface IRunRepository {

    fun insertRun(run : Run)
    fun deleteRun(run : Run)

    // suspend
    fun getAllRunsSortedByDate()
    fun getAllRunsSortedByDistance()
    fun getAllRunsSortedByTimeinMillis()
    fun getAllRunsSortedByAvgSpeed()
    fun getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed()
    fun getTotalCaloriesBurned()
    fun getTotalTimeinMillis()
    fun getTotalDistance()

    fun testFun() : String



}