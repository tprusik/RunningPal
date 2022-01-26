package com.example.runningpal.tests.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.*
import com.example.runningpal.others.DbConstants
import com.example.runningpal.repositories.IRunRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FakeRunRepository : IRunRepository {

    override fun insertRun(run: Run) {}

    override fun deleteRun(run: Run) {
        TODO("Not yet implemented")
    }

    override fun createRoom(room: Room) {
        TODO("Not yet implemented")
    }

    override fun updateRoomState(room: Room) {
        TODO("Not yet implemented")
    }

    override fun deleteRoom(room: Room) {
        TODO("Not yet implemented")
    }

    override fun insertRoomHistory(roomHistory: RoomHistory) {
        TODO("Not yet implemented")
    }

    override fun addRunnerToRoom(runner: Runner) {
        TODO("Not yet implemented")
    }

    override fun getRoom(roomID: String): LiveData<Room> {
        TODO("Not yet implemented")
    }

    override fun getRunners(roomID: String): LiveData<List<Runner>> {
        TODO("Not yet implemented")
    }

    override fun getRoomState(roomID: String): LiveData<Room> {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByDate(): LiveData<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByDistance(): LiveData<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>> {
        TODO("Not yet implemented")
    }

    override fun getTotalStatistics(userID: String): LiveData<RunStatistics> {

        var totalStatistics = MutableLiveData<RunStatistics>()

        var totRuns  = 10
        var  totDistance = 100
        var totCaloriesBurned = 100
        var totTime = 100L


        var totStats = RunStatistics(totRuns,totDistance,totCaloriesBurned,totTime)
        totalStatistics.postValue(totStats)


        return totalStatistics

    }

    override fun updateRoomTime(time: Int, id: String) {
        TODO("Not yet implemented")
    }

    override fun getRoomHistory(): LiveData<List<RoomHistory>> {
        TODO("Not yet implemented")
    }

}