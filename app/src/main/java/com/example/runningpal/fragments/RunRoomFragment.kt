package com.example.runningpal.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.Room
import com.example.runningpal.db.Runner
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningpal.others.Constants.ACTION_STOP_SERVICE
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.Polyline
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunnerCardAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_run_room.*
import org.koin.android.ext.android.get
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class RunRoomFragment : Fragment() {

    private lateinit var  users : List<User>
    private lateinit var runnersViewModel : RunnersViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var runnersAdapter  : RunnerCardAdapter
    private var pathPoints = mutableListOf<Polyline>()
    private lateinit var uid : String
    private lateinit var dialog : ChooseUserDialogFragment
    private  var runningTimeToEnd  = 10000
    private  var isStarted = false
    private lateinit  var runners : List<Runner>
    private var  runnersData = arrayListOf<String>()

    companion object {
        var userss  = mutableListOf<User>()
        var user = User()
        var room = Room()
        var runner  = MutableLiveData<Runner>()
        var isStarted  = MutableLiveData<Boolean>()
        var isCountingDown  = MutableLiveData<Boolean>()
        var isIAmCreator = false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.run_room_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.room_menu_new_room -> {
                createNewRoom()
                item.setVisible(false)
                runRoomNavBar.visibility = LinearLayout.VISIBLE
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

        initVal()
        setupRecyclerView()
        dialog = ChooseUserDialogFragment()
        subscribeObserver()
        users = userss

        if(isIAmCreator) { buttonVisibilityJoinToRoom() }

        if(room.id!=null) {
            Timber.d("nie kurde jet null")
            observeRoom() }

        run_room_add_runner.setOnClickListener{

            dialog.submitList(users)
            dialog.show(childFragmentManager, "NoticeDialogFragment")
        }

        runRoomNavBarRunningType.setOnClickListener {
            chooseRunTypeDialogBuilder()
        }

        run_room_remove_room.setOnClickListener{ confirmRemoveRoomDialogBuilder() }

        run_room_start.setOnClickListener{
            mainViewModel.updateRoomStateStarted(room)
        }

    }



    fun createNewRoom(){

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val roomID = UUID.randomUUID().toString()
        val currentDate = sdf.format(Date())
        room = Room(uid, 0, currentDate, false)

        val runner = Runner(uid, uid, user.nick, user.profilePic, 0f, 0)

        mainViewModel.createRoom(room)
        mainViewModel.addRunnerToRoom(runner)

        observeRoom() }

    fun initVal(){
        mainViewModel = get()
        runnersViewModel = get()
        uid = FirebaseAuth.getInstance().currentUser!!.uid
        runners = mutableListOf<Runner>() }

    fun setPrimaryState(){
        isStarted = false
        runners = mutableListOf<Runner>()
        isIAmCreator = false }

    fun subscribeObserver(){

        TrackingService.timeRunInSeconds.observe(viewLifecycleOwner, Observer {

            if (runningTimeToEnd * 5 <= it) {

                sendCommandToService(ACTION_STOP_SERVICE)
                fillRunnersArray()

                finishRunDialogBuilder()

                Timber.d("KONIEC")
            }
        })



        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            calculateRunDataAndSaveToDb()
        })

        ChooseUserDialogFragment.ids.observe(viewLifecycleOwner, Observer {
            val user = users.get(it)

            val invitation = Invitation(uid, user.uid, room.id)
            runnersViewModel.sendRunInvitation(invitation)
            Timber.d("Wysłano zaproszenie")
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

            mainViewModel.getRoomState(room.id!!).observe(viewLifecycleOwner, Observer {

                tvRunRoomTypeTimeInput.setText(it.timeToEnd.toString())
                runningTimeToEnd = it.timeToEnd!!

                if (it.isStarted == true && isStarted == false) {
                    isStarted = true
                    startRunCountDown()
                }
            })

            mainViewModel.getRunners(room.id!!).observe(viewLifecycleOwner, Observer {

                runnersAdapter.submitList(it)
                runners = it
                Timber.d("mam cię $    !!!" + it.size.toString() + "  !!! " + it.last().distanceMetres.toString())
            })
    }

   private fun setupRecyclerView() = rvRunRoom.apply {
        runnersAdapter = RunnerCardAdapter()
        adapter = runnersAdapter
        layoutManager = LinearLayoutManager(requireContext()) }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it) }

    private fun calculateRunDataAndSaveToDb(){
        var distanceInMeters = 0

        for(polyline in pathPoints) {
            distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt() }

       // val avgSpeed = Math.round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f
        val runner = Runner(uid, room.id, user.nick, user.profilePic, 0f, distanceInMeters)
        Timber.d("calculate and save ")
        mainViewModel.addRunnerToRoom(runner)
    }


    fun chooseRunTypeDialogBuilder(){

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Wybierz typ rywalizacji !")

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



    fun finishRunDialogBuilder(){

       // val view = requireView().findViewById(R.id.ivFinishRunDialog) as ImageView
        val inflater = layoutInflater
        val dialogLayout: View = inflater.inflate(R.layout.finish_run_dialog_image_view, null)

        MaterialAlertDialogBuilder(requireContext())
                .setTitle("GRATULACJE KONIEC")
                .setView(dialogLayout)
                .setItems(runnersData.toTypedArray(), DialogInterface.OnClickListener { dialog, which -> }
                )
                .setPositiveButton("ZAKONCZ") { dialog, which ->
                    navHostFragment.findNavController().navigate(R.id.action_runRoomFragment_self)
                }
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
                tick--

            }

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

    fun buttonVisibilityReadyToRunTimeType(){

        runRoomNavBarRunningType.visibility = Button.GONE
        run_room_start.visibility = Button.VISIBLE
        tvRunRoomTypeTime.visibility = Button.VISIBLE
        tvRunRoomTypeTimeInput.visibility = Button.VISIBLE

    }

    fun buttonVisibilityJoinToRoom(){
        runRoomNavBar.visibility = LinearLayout.VISIBLE
        runRoomNavBarRunningType.visibility = Button.GONE
        run_room_start.visibility = Button.GONE

        tvRunRoomTypeTime.visibility = Button.VISIBLE
        tvRunRoomTypeTimeInput.visibility = Button.VISIBLE

    }


}
