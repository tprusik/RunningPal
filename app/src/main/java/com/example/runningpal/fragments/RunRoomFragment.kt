package com.example.runningpal.fragments

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.Room
import com.example.runningpal.db.Runner
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.Polyline
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunnerCardAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
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

    companion object   {
        var users  = mutableListOf<User>()
        var user = User()
        var room = Room()
        var runner  = MutableLiveData<Runner>() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val dialog = ChooseUserDialogFragment()
        subscribeObserver()

        if(room.id!=null) {
            Timber.d("nie kurde jet null")
            observeRoom()
            changeButtonVisibility()
        }

        btnCreateRoomRunFragment.setOnClickListener {
            createNewRoom()
            readyToRunRunDialogBuilder()
        }

        btnStartRunRoomRunFragment.setOnClickListener{




            startRunCountDown()
           // sendCommandToService(ACTION_START_OR_RESUME_SERVICE)

        }

        btnInviteRunRoomFragment.setOnClickListener {
            dialog.submitList(users)
            dialog.show(childFragmentManager, "NoticeDialogFragment")

        }
    }

    private fun changeButtonVisibility(){

            btnCreateRoomRunFragment.visibility = FloatingActionButton.GONE
            btnInviteRunRoomFragment.visibility = Button.VISIBLE
            btnStartRunRoomRunFragment.visibility = Button.VISIBLE 
            tvRunRoomCountDown.visibility = TextView.VISIBLE
            tvRunRoomCountDownInput.visibility = TextView.VISIBLE }


    fun createNewRoom(){

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val roomID = UUID.randomUUID().toString()
        val currentDate = sdf.format(Date())
        room = Room(uid, currentDate, false)

        val runner = Runner(uid, uid, user.nick, user.profilePic, 0f, 0)

        mainViewModel.createRoom(room)
        mainViewModel.addRunnerToRoom(runner)

        changeButtonVisibility()
        observeRoom()
    }

    fun initVal(){
        mainViewModel = get()
        runnersViewModel = get()
        uid = FirebaseAuth.getInstance().currentUser!!.uid }

    fun subscribeObserver(){

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

            mainViewModel.getRunners(room.id!!).observe(viewLifecycleOwner, Observer {
                runnersAdapter.submitList(it)
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

    fun readyToRunRunDialogBuilder(){
        MaterialAlertDialogBuilder(requireContext())
                .setTitle("Start Biegu !")
                .setMessage("Oczekiwanie na wszystkich zawodników...")
                .setPositiveButton("Jestem gotowy !") { dialog, which ->
                    // Respond to positive button press
                }
                .show()
    }

    fun startRunCountDown(){

        var tick = 10


        object : CountDownTimer(10000, 1000) {

            override fun onTick(millisUntilFinished: Long) {

                tvRunRoomCountDownInput.setText(tick.toString())
                tick--

            }

            override fun onFinish() {
                room.isStarted = true
                mainViewModel.updateRoomStateStarted(room)
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE)
            }

        }.start()


    }



    }
