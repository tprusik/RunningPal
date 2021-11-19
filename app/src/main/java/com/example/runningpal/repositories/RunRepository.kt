package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.DbConstants.DB_NODE_RUN
import com.example.runningpal.db.Run
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.db.User
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
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference(DB_NODE_RUN).child(uid).push().setValue(run)

    }

    override fun deleteRun(run: Run) {
        TODO("Not yet implemented")
    }

    //pobierz po prostu wszystkie w kolejnosci jakie sa w bazie
    override fun getAllRunsSortedByDate() : LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("timestamp")
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

    override fun getAllRunsSortedByDistance(): LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("distanceMetres")
                .addValueEventListener(object : ValueEventListener {

                    val listr =  mutableListOf<Run>()

                    override fun onDataChange(snapshot: DataSnapshot) {

                        for(snap in snapshot.children){

                            val run = snap.getValue(Run::class.java)!!
                            listr.add(run)
                            Timber.d("sss" + run.avgSpeedKmh.toString())

                            runs.postValue(listr)

                        }

                    }

                    override fun onCancelled(error: DatabaseError) {}

                })

        return runs

    }

    override fun getAllRunsSortedByTimeinMillis(): LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("timeInMilis")
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

    override fun getAllRunsSortedByAvgSpeed(): LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("avgSpeedKmh")
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

    override fun getTotalAvgSpeed() {

    }

    override fun getTotalCaloriesBurned() {
        TODO("Not yet implemented")
    }

    override fun getTotalTimeinMillis() {
        TODO("Not yet implemented")
    }

    override fun getTotalDistance() : LiveData<Int> {


        var totalDistance = MutableLiveData<Int>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
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

                        Timber.d(run.avgSpeedKmh.toString())

                    }

                    totalDistance.postValue(totDistance)

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return totalDistance
    }

    override fun getTotalStatistics(): LiveData<RunStatistics> {

        var totalStatistics = MutableLiveData<RunStatistics>()

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
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

                        Timber.d(run.avgSpeedKmh.toString())

                    }

                    var totStats = RunStatistics(totRuns,totDistance,totCaloriesBurned,totTime)

                    totalStatistics.postValue(totStats)

                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return totalStatistics

    }


}