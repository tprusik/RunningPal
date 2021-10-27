package com.example.runningpal.repositories

import com.example.runningpal.db.Run

class RunRepository : IRunRepository  {
    override fun insertRun(run: Run) {
        TODO("Not yet implemented")
    }

    override fun deleteRun(run: Run) {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByDate() {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByDistance() {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByTimeinMillis() {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByAvgSpeed() {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByCaloriesBurned() {
        TODO("Not yet implemented")
    }

    override fun getTotalAvgSpeed() {
        TODO("Not yet implemented")
    }

    override fun getTotalCaloriesBurned() {
        TODO("Not yet implemented")
    }

    override fun getTotalTimeinMillis() {
        TODO("Not yet implemented")
    }

    override fun getTotalDistance() {
        TODO("Not yet implemented")
    }

    override fun testFun(): String {

        return "dziala"
    }


}