package com.example.runningpal.ui.viewmodels
import android.graphics.Bitmap
import android.util.Base64
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runningpal.db.Run
import com.example.runningpal.others.RunSortType
import com.example.runningpal.repositories.IRunRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream


class MainViewModel(val repo: IRunRepository) : ViewModel() {


    private val runSortedByDate = repo.getAllRunsSortedByDate()
    private  val runSortedByDistance = repo.getAllRunsSortedByDistance()
    private  val runSortedByCaloriesBurned = repo.getAllRunsSortedByCaloriesBurned()
    private  val runSortedByTimeInMillis = repo.getAllRunsSortedByTimeinMillis()
    private  val runSortedByAvgSpeed = repo.getAllRunsSortedByAvgSpeed()


    val runs = MediatorLiveData<List<Run>>()

    var sortType = RunSortType.DATE

    init {

        runs.addSource(runSortedByDate) { result ->
            if(sortType == RunSortType.DATE) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(runSortedByAvgSpeed) { result ->
            if(sortType == RunSortType.AVGSPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByCaloriesBurned) { result ->
            if(sortType == RunSortType.CALORIESBURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByDistance) { result ->
            if(sortType == RunSortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runSortedByTimeInMillis) { result ->
            if(sortType == RunSortType.RUNNING_TIME) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortType: RunSortType) = when(sortType) {
        RunSortType.DATE -> runSortedByDate.value?.let { runs.value = it }
        RunSortType.RUNNING_TIME -> runSortedByTimeInMillis.value?.let { runs.value = it }
        RunSortType.AVGSPEED -> runSortedByAvgSpeed.value?.let { runs.value = it }
        RunSortType.DISTANCE -> runSortedByDistance.value?.let { runs.value = it }
        RunSortType.CALORIESBURNED -> runSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }


    fun saveRunToBase(run: Run) = viewModelScope.launch { repo.insertRun(run)}

    fun saveTrackSnapshotToBase(bmp: Bitmap?, id: String)  =  viewModelScope.launch  {

            val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(id)

        val bao = ByteArrayOutputStream()
        bmp!!.compress(Bitmap.CompressFormat.PNG, 100, bao) // bmp is bitmap from user image file

        bmp.recycle()
        val byteArray = bao.toByteArray()
       // val imageB64: String = Base64.encodeToString(byteArray, Base64.URL_SAFE)


            sRef.putBytes(byteArray).addOnSuccessListener {

                Timber.d("Udalo sie wgrac ")
            }


    }

}