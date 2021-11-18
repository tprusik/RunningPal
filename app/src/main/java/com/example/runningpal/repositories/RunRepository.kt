package com.example.runningpal.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.runningpal.db.Run
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
        val myRef = database.getReference("Runs").child(uid).push().setValue(run)

    }

    override fun deleteRun(run: Run) {
        TODO("Not yet implemented")
    }

    //pobierz po prostu wszystkie w kolejnosci jakie sa w bazie
    override fun getAllRunsSortedByDate() : LiveData<List<Run>> {
        var  runs  = MutableLiveData<List<Run>>()


        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance("https://mywork-e32c4-default-rtdb.europe-west1.firebasedatabase.app/")
        val myRef = database.getReference("Runs").child(uid).orderByChild("timestamp").limitToFirst(1)
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
        val myRef = database.getReference("Runs").child(uid).orderByChild("distanceMetres").limitToFirst(2)
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
        val myRef = database.getReference("Runs").child(uid).orderByChild("timeInMilis").limitToFirst(3)
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
        val myRef = database.getReference("Runs").child(uid).orderByChild("avgSpeedKmh").limitToFirst(4)
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
        val myRef = database.getReference("Runs").child(uid).orderByChild("caloriesBurnt").limitToLast(3)
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

    override fun getTotalDistance() {
        TODO("Not yet implemented")
    }

    override fun testFun(): String {

        return "dziala"
    }


}