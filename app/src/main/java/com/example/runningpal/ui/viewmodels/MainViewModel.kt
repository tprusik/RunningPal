package com.example.runningpal.ui.viewmodels
import android.graphics.Bitmap
import android.widget.Toast
import androidx.lifecycle.*
import com.example.runningpal.db.Room
import com.example.runningpal.db.RoomHistory
import com.example.runningpal.db.Run
import com.example.runningpal.db.Runner
import com.example.runningpal.others.FiltrRunType
import com.example.runningpal.others.RunSortType
import com.example.runningpal.others.Utils.getLastMonthInMillis
import com.example.runningpal.others.Utils.getThisMonthInMillis
import com.example.runningpal.others.Utils.getThisWeekInMillis
import com.example.runningpal.repositories.IRunRepository
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_run.view.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel(val repo: IRunRepository) : ViewModel() {

    private val runSortedByDate = repo.getAllRunsSortedByDate()
    private  val runSortedByDistance = repo.getAllRunsSortedByDistance()
    private  val runSortedByCaloriesBurned = repo.getAllRunsSortedByCaloriesBurned()
    private  val runSortedByTimeInMillis = repo.getAllRunsSortedByTimeinMillis()
    private  val runSortedByAvgSpeed = repo.getAllRunsSortedByAvgSpeed()
    
    fun getUserStatistics(userID: String) = repo.getTotalStatistics(userID)

    val roomHistory = repo.getRoomHistory()

    val runs = MediatorLiveData<List<Run>>()

    var sortType = RunSortType.DATE
    var filtrType = FiltrRunType.INCREASE


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

    fun filtrRuns(filtrType: FiltrRunType) = when(filtrType) {

        FiltrRunType.INCREASE -> runs.value?.let { runs.value = it.reversed() }

        FiltrRunType.DECREASE -> runs.value?.let { runs.value = it.reversed() }

    }.also {
        this.filtrType = filtrType
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

    fun addRunnerToRoom(runner: Runner) =  viewModelScope.launch { repo.addRunnerToRoom(runner) }
    fun createRoom(room: Room) = viewModelScope.launch { repo.createRoom(room) }
    fun getRoom(idRoom: String) = repo.getRoom(idRoom)
    fun getRunners(idRoom: String) = repo.getRunners(idRoom)
    fun updateRoomStateStarted(room: Room) = viewModelScope.launch { repo.updateRoomState(room) }
    fun removeRoom(room: Room)  = viewModelScope.launch { repo.deleteRoom(room)}
    fun getRoomState(roomID: String) = repo.getRoomState(roomID)
    fun insertRoomTimeToEnd(room: Room) =viewModelScope.launch { repo.updateRoomTime(room.timeToEnd!!, room.id!!)}

    fun insertRoomHistory(room: RoomHistory)=viewModelScope.launch { repo.insertRoomHistory(room)}


    //Distance
    fun sortRunByDistanceGreaterThan(distance: Long){
        runs.value?.let{runs.value = it.filter { it.distanceMetres >= distance } } }

    fun sortRunByDistanceSmallerThan(distance: Long){
        runs.value?.let{runs.value = it.filter { it.distanceMetres <= distance } } }

    //Time
    fun sortRunByTimeGreaterThan(value: Long){
        runs.value?.let{runs.value = it.filter { it.timeInMilis >= value*60000 } } }

    fun sortRunByTimeSmallerThan(value: Long){
        runs.value?.let{runs.value = it.filter { it.timeInMilis <= value*60000 } } }

    // Calories
    fun sortRunByCaloriesGreaterThan(values: Int){
        runs.value?.let{runs.value = it.filter { it.caloriesBurned >= values } } }

    fun sortRunByCaloriesSmallerThan(values: Int){
        runs.value?.let{runs.value = it.filter { it.caloriesBurned <= values } } }

    // Speed
    fun sortRunBySpeedGreaterThan(values: Int){
        runs.value?.let{runs.value = it.filter { it.avgSpeedKmh >= values.toFloat() } } }

    fun sortRunBySpeedSmallerThan(values: Int){
        runs.value?.let{runs.value = it.filter { it.avgSpeedKmh <= values.toFloat() } } }
    //Date
    fun sortRunByDateGreaterThan(value: String){

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        try {
            val date = dateFormat.parse(value)
            val milliseconds = date.time

            Timber.d("test daty " + milliseconds.toString() + "obiekt ")
            runs.value?.let{
                runs.value = it.filter { it.timestamp >= milliseconds } }
        } catch (e: ParseException ) {
            e.printStackTrace()
        } }

    fun sortRunByDateSmallerThan(value: String){

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        try {
            Timber.d("test")

            val date = dateFormat.parse(value)
            Timber.d("test1")

            val milliseconds = date.time
            Timber.d("test2")

            runs.value?.let{
                runs.value = it.filter { it.timestamp <= milliseconds } }


        } catch (e: ParseException ) {
            e.printStackTrace()
            Timber.d("tydzia")

        } }

    fun showDateSmaller(value : Long){
        runs.value?.let{
        runs.value = it.filter { it.timestamp <= value } }


    }

    fun showDateGreater(value : Long){
        runs.value?.let{
        runs.value = it.filter { it.timestamp >= value } }}

    fun sortDateByThisWeek(){
        var weekInMillis = getThisWeekInMillis()
        var startOfWeekInMillis = weekInMillis[0]
        var endOfWeekInMillis = weekInMillis[1]

        Timber.d("test daty" + startOfWeekInMillis.toString())

        showDateSmaller(endOfWeekInMillis)
        showDateGreater(startOfWeekInMillis)
    }

    fun sortDateByThisMonth(){
        var monthInMillis = getThisMonthInMillis()
        var startOfMonthInMillis = monthInMillis[0]
        var endOfMonthInMillis = monthInMillis[1]

        showDateGreater(startOfMonthInMillis)
        showDateSmaller(endOfMonthInMillis)

    }

    fun sortDateByLastMonth(){
        var monthInMillis = getLastMonthInMillis()
        var startOfMonthInMillis = monthInMillis[0]
        var endOfMonthInMillis = monthInMillis[1]

        showDateGreater(startOfMonthInMillis)
        showDateSmaller(endOfMonthInMillis)

    }


}