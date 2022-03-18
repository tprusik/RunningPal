package com.example.runningpal.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.db.*
import com.example.runningpal.others.Constants.ACTION_PAUSE_SERVICE
import com.example.runningpal.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.Polyline
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RoomHistoryAdapter
import com.example.runningpal.ui.adapters.RunnerCardAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_run_room.*
import org.koin.android.ext.android.get
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.round
import kotlin.math.roundToInt


class RunRoomFragment : Fragment() {

    private lateinit var  users : List<User>
    private lateinit var runnersViewModel : RunnersViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var runnersAdapter  : RunnerCardAdapter
    private lateinit var historyAdapter  : RoomHistoryAdapter
    private var pathPoints = mutableListOf<Polyline>()
    private lateinit var uid : String
    private lateinit var dialog : ChooseUserDialogFragment
    private  var runningTimeToEnd  = 10000
    private lateinit  var runners : List<Runner>
    private lateinit  var roomHistory : List<RoomHistory>
    private var  runnersData = arrayListOf<String>()

    private var distanceProgress = mutableListOf<Int>()
    private var timeProgress = mutableListOf<Long>()
    private var placeProgress = mutableListOf<Int>()
    private var avgSpeedProgress = mutableListOf<Float>()


    companion object {
        var userss  = mutableListOf<User>()
        var user = User()
        var room = Room()
        var runner  = MutableLiveData<Runner>()

        var isRoomCreated  = MutableLiveData<Boolean>()
        var isStarted  = MutableLiveData<Boolean>()
        var isIAmCreator = false
        var joinedToRoom = false

        var isCountingDown  = MutableLiveData<Boolean>()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(joinedToRoom)  setHasOptionsMenu(false)
        else setHasOptionsMenu(true)

        initVal()
        Timber.d("sprawdzamm ") }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.run_room_menu, menu) }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.room_menu_new_room -> {

                if(TrackingService.isTracking.value == true){
                    view?.let {
                        Snackbar.make(it, "Musisz zakończyć bieg aby stworzyć nowy pokój", Snackbar.LENGTH_LONG)
                                .setAction("ROZUMIEM") {
                                }
                                .show()
                    }}

                else {
                    createNewRoom()
                    rvRunRoom.adapter = runnersAdapter
                    item.setVisible(false)
                    runRoomNavBar.visibility = LinearLayout.VISIBLE
                }
                true
            }
            else -> {true}
        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run_room, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        dialog = ChooseUserDialogFragment()
        subscribeObserver()
        users = userss

        if(room.id!=null) {

            observeRoom() }

        run_room_add_runner.setOnClickListener{
            dialog.submitList(users)
            dialog.show(childFragmentManager, "NoticeDialogFragment")
        }

        runRoomNavBarRunningType.setOnClickListener {
            chooseRunTimeDialogBuilder()
        }

        run_room_remove_room.setOnClickListener {
            confirmRemoveRoomDialogBuilder()
        }

        run_room_start.setOnClickListener{
            mainViewModel.updateRoomStateStarted(room)
        }

      if(joinedToRoom)
          buttonVisibilityJoinedToRoom()
          R.id.room_menu_new_room
       // rvRunRoom.adapter = runnersAdapter

    }


    fun createNewRoom(){

        isIAmCreator = true
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val roomID = UUID.randomUUID().toString()
        val currentDate = sdf.format(Date())
        room = Room(uid, 0, currentDate, false)


        isRoomCreated.postValue(true)


        Timber.d("sprawdzam "+ isRoomCreated.toString())


        val runner = Runner(uid, uid, user.nick, user.profilePic, 0f, 0)

        mainViewModel.createRoom(room)
        mainViewModel.addRunnerToRoom(runner)

        observeRoom() }

    fun initVal(){
        mainViewModel = get()
        runnersViewModel = get()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        runners = mutableListOf<Runner>()
        roomHistory = mutableListOf<RoomHistory>()
         isStarted.postValue(false)
        //isRoomCreated.postValue(false)
        //isIAmCreator = false
        }

    fun setPrimaryState(){
        isStarted.postValue(false)
        isRoomCreated.postValue(false)
        runners = mutableListOf<Runner>()
        isIAmCreator = false
        runners = mutableListOf<Runner>()
        mainViewModel = get()
        runnersViewModel = get()
        room = Room()
        joinedToRoom = false}

    fun subscribeObserver(){

        isRoomCreated.observe(viewLifecycleOwner, Observer {
            if(!it) buttonVisibilityRoomRemoved()
            if(it && isIAmCreator)  buttonVisibilityRoomCreated()
            if(it && !isIAmCreator) buttonVisibilityJoinedToRoom()
        })


        TrackingService.timeRunInSeconds.observe(viewLifecycleOwner, Observer {
            if (runningTimeToEnd * 5 <= it && isRoomCreated.value==true && TrackingService.isTracking.value == true) {

                Timber.d("hej to ja ")

                sendCommandToService(ACTION_PAUSE_SERVICE)

                fillRunnersArray()

                finishRunDialogBuilder()
                Timber.d("KONIEC")
            }
        })

       mainViewModel.roomHistory.observe(viewLifecycleOwner, Observer {

           historyAdapter.submitList(it)
       })

        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {

            pathPoints = it

            if(pathPoints.size>0 && isRoomCreated.value == true )
            calculateRunDataAndSaveToDb()
        })

        ChooseUserDialogFragment.ids.observe(viewLifecycleOwner, Observer {

            if(users.size>0) {
                val user = users.get(it)
                val invitation = Invitation(uid, user.uid, room.id)
                runnersViewModel.sendRunInvitation(invitation)
                Timber.d("Wysłano zaproszenie")
            }
        })

        runnersViewModel.user.observe(viewLifecycleOwner, Observer {
            user = it

            it.contacts?.also {
                runnersViewModel.getSelectedRunners(it).observe(viewLifecycleOwner,
                        Observer {
                            users = it
                        })
            }

            Timber.d("add Runner ${user.nick} ")
        })

    }




    fun observeRoom(){

            mainViewModel.getRoomState(room.id!!).observe(viewLifecycleOwner, {

                Timber.d("obs pokoj")
                tvRunRoomTypeTimeInput.setText(it.timeToEnd.toString())
                runningTimeToEnd = it.timeToEnd!!

                if (it.isStarted == true && isStarted.value == false) {
                    isStarted.postValue(true)
                    startRunCountDown() }

            })

            mainViewModel.getRunners(room.id!!).observe(viewLifecycleOwner, Observer {

                it?.let{
                    runnersAdapter.submitList(it)
                    runners = it
                }

              //  runnersAdapter.submitList(it)
               // runners = it
               // Timber.d("mam cię $    !!!" + it.size.toString() + "  !!! " + it.last().distanceMetres.toString())
            })
    }

   private fun setupRecyclerView() = rvRunRoom.apply {
        runnersAdapter = RunnerCardAdapter()
        historyAdapter = RoomHistoryAdapter()
        adapter = historyAdapter
        layoutManager = LinearLayoutManager(requireContext()) }

    private fun changeRecyclerViewAdapter() = rvRunRoom.apply {
        Timber.d("nowy ja")
        historyAdapter.submitList(roomHistory)
        adapter = historyAdapter
        layoutManager = LinearLayoutManager(requireContext()) }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it) }

    private fun calculateRunDataAndSaveToDb(){
        var distanceInMeters = 0
        var timeInSeconds = 0L;
        for(polyline in pathPoints) {
            distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt() }

        TrackingService.timeRunInSeconds.value?.let {
            timeProgress.add(it)
            distanceProgress.add(distanceInMeters)
            placeProgress.add(runners.indexOf(runner.value)+2)
            timeInSeconds = it }

        val avgSpeed = Math.round((distanceInMeters / 1000f) / (timeInSeconds / 1000f / 60 / 60) * 10) / 10f
        avgSpeedProgress.add(avgSpeed)

        val runner = Runner(uid, room.id, user.nick, user.profilePic, avgSpeed.roundToInt().toFloat(), distanceInMeters,distanceProgress,timeProgress,placeProgress,avgSpeedProgress)
        Timber.d("calculate and save ")

        mainViewModel.addRunnerToRoom(runner) }
    
    fun chooseRunTypeDialogBuilder(){

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Wybierz czas rywalizacji !")

                .setItems(resources.getStringArray(R.array.run_type_options), DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        0 -> {
                            chooseRunTimeDialogBuilder()
                        }
                        1 -> {
                        }
                        2 -> {
                        }
                    }
                })
                .show() }


    fun setInitValueAfterRun(){

        isStarted.postValue(false)
        isRoomCreated.postValue(false)
        isIAmCreator = false }

    fun finishRunDialogBuilder(){

       // val view = requireView().findViewById(R.id.ivFinishRunDialog) as ImageView
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.finish_run_dialog_image_view, null)
        val drawable  = resources.getDrawable(R.drawable.finish_run)

        changeRecyclerViewAdapter()

        mainViewModel.insertRoomHistory(RoomHistory(room.timestamp,runners,(runners.indexOf(runner.value) + 2).toString()))
        mainViewModel.removeRoom(room)

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("GRATULACJE KONIEC")
                .setPositiveButton("ZAKONCZ") { dialog, which ->

                    buttonVisibilityInitial()

                    setPrimaryState()
                    setupRecyclerView()
                    navHostFragment.findNavController().navigate(R.id.trackingFragment) }

                //.setBackground(drawable)

                .setItems(runnersData.toTypedArray(), DialogInterface.OnClickListener { dialog, which -> })
                .show()
    }

    fun chooseRunTimeDialogBuilder(){

        val editText = EditText(requireContext())

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Wybierz czas rywalizacji")
                .setView(editText)
                .setPositiveButton("Zatwierdź") { dialog, which ->
                   buttonVisibilityReadyToRunTimeType()

                    room.timeToEnd =  editText.text.toString().toInt()
                    mainViewModel.insertRoomTimeToEnd(room)

                }
                .show() }


    fun confirmRemoveRoomDialogBuilder(){
        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Czy chcesz usunąć pokój ?!")
                .setMessage("Wszelkie dane zostaną utracone...")
                .setPositiveButton("Tak !") { dialog, which ->

                    mainViewModel.removeRoom(room)
                    initVal()
                    setInitValueAfterRun()
                    buttonVisibilityInitial()

                    //sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
                    //navHostFragment.findNavController().navigate(R.id.trackingFragment)


                    mainViewModel.removeRoom(room)
                    isRoomCreated.postValue(false)
                }
                .setNegativeButton("Nie !") { dialog, which ->

                }
                .show() }

    fun startRunCountDown(){
        var tick = 10

        tvRunRoomCountDown.visibility = TextView.VISIBLE
        tvRunRoomCountDownInput.visibility = TextView.VISIBLE

        object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tvRunRoomCountDownInput.setText(tick.toString())
                tick-- }

            override fun onFinish() {
                tvRunRoomCountDown.visibility = TextView.GONE
                tvRunRoomCountDownInput.visibility = TextView.GONE

                room.isStarted = true
                mainViewModel.updateRoomStateStarted(room)

                sendCommandToService(ACTION_START_OR_RESUME_SERVICE) }

        }.start()
    }

    fun fillRunnersArray(){

        var place = 1
        for(runner in runners){
            runnersData.add("${place}  ${runner.name!!} dystans ${runner.distanceMetres!!}  ")
            place++
        }

    }


    fun buttonVisibilityRoomRemoved(){
        runRoomNavBar.visibility = LinearLayout.GONE
    }

    fun buttonVisibilityInitial(){


        runRoomNavBar.visibility = LinearLayout.GONE
        run_room_start.visibility = Button.VISIBLE
        run_room_remove_room.visibility = Button.VISIBLE
        run_room_add_runner.visibility = Button.VISIBLE

        tvRunRoomTypeTime.visibility = Button.GONE
        tvRunRoomTypeTimeInput.visibility = Button.GONE
    }

    fun buttonVisibilityRoomCreated(){
        runRoomNavBar.visibility = LinearLayout.VISIBLE
        runRoomNavBarRunningType.visibility = Button.VISIBLE
        run_room_start.visibility = Button.GONE
        run_room_remove_room.visibility = Button.VISIBLE

       // tvRunRoomTypeTime.visibility = Button.GONE
       // tvRunRoomTypeTimeInput.visibility = Button.VISIBLE
    }

    fun buttonVisibilityReadyToRunTimeType(){
        runRoomNavBar.visibility = LinearLayout.VISIBLE
        runRoomNavBarRunningType.visibility = Button.GONE
        run_room_start.visibility = Button.VISIBLE
        tvRunRoomTypeTime.visibility = Button.VISIBLE
        tvRunRoomTypeTimeInput.visibility = Button.VISIBLE
        run_room_remove_room.visibility = Button.VISIBLE
    }



    fun buttonVisibilityJoinedToRoom(){
        isRoomCreated.postValue(true)


        runRoomNavBar.visibility = LinearLayout.VISIBLE
        runRoomNavBarRunningType.visibility = Button.GONE
        run_room_start.visibility = Button.GONE
        run_room_remove_room.visibility = Button.GONE
        run_room_add_runner.visibility = Button.GONE
        tvRunRoomTypeTime.visibility = Button.VISIBLE
        tvRunRoomTypeTimeInput.visibility = Button.VISIBLE
    }


}
