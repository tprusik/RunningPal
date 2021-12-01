package com.example.runningpal.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.style.TtsSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.Room
import com.example.runningpal.db.Runner
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants
import com.example.runningpal.others.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.runningpal.others.DatabaseUtility
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunnerCardAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_run_room.*
import org.koin.android.ext.android.get
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class RunRoomFragment : Fragment() {

    private lateinit var  users : List<User>
    private lateinit var runnersViewModel : RunnersViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var roomID : String
    private lateinit var user : User
    private lateinit var runnersAdapter  : RunnerCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFriends()
        mainViewModel = get()
        setupRecyclerView()
        val dialog = ChooseUserDialogFragment()
        subscribeObserver()


        btnCreateRoomRunFragment.setOnClickListener {
           roomID =  UUID.randomUUID().toString()

            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())

            val room = Room(roomID,currentDate,false)
            val runner = Runner("rVZAsWtKgpeAHaeIN8vTlxSdcpk1",roomID,user.nick,user.profilePic,0f,0)

            mainViewModel.createRoom(room)

            mainViewModel.addRunnerToRoom(runner)


            observeRoom()

            btnCreateRoomRunFragment.visibility = FloatingActionButton.GONE
            btnStartRunRoomRunFragment.visibility = FloatingActionButton.VISIBLE
        }

        btnStartRunRoomRunFragment.setOnClickListener{


            sendCommandToService(ACTION_START_OR_RESUME_SERVICE)

        }

        btnInviteRunRoomFragment.setOnClickListener {
            dialog.submitList(users)
            dialog.show(childFragmentManager, "NoticeDialogFragment")
        }

    }

    fun getFriends(){
        users = mutableListOf<User>()
        runnersViewModel = get()

        runnersViewModel.runners.observe(viewLifecycleOwner, Observer {
            users = mutableListOf<User>()
            val cont = it.contacts!!.toSet()

            runnersViewModel.getSelectedRunners(cont.toList()).observe(viewLifecycleOwner, Observer {

                users = it

            })


        })

    }

    fun subscribeObserver(){

        ChooseUserDialogFragment.ids.observe(viewLifecycleOwner, Observer {
                val user = users.get(it)

                val invitation = Invitation(user.uid,"rVZAsWtKgpeAHaeIN8vTlxSdcpk1","1")

                runnersViewModel.sendInvitation(invitation)
                Timber.d("Wysłano zaproszenie")

        })

        runnersViewModel.user.observe(viewLifecycleOwner, Observer {

            user = it

            Timber.d("add Runner ${user.nick}  ")

        })

    }

    fun observeRoom(){

    mainViewModel.getRoom(roomID).observe(viewLifecycleOwner, Observer {

        Timber.d("strzelilem ")
    })

        mainViewModel.getRunners(roomID).observe(viewLifecycleOwner, Observer {

            Timber.d("strzelilem ")

            runnersAdapter.submitList(it)
        })

    }

   private fun setupRecyclerView() = rvRunRoom.apply {

        runnersAdapter = RunnerCardAdapter()
        adapter = runnersAdapter
        layoutManager = LinearLayoutManager(requireContext())

    }

    private fun sendCommandToService(action: String) =
            Intent(requireContext(), TrackingService::class.java).also {
                it.action = action
                requireContext().startService(it)
            }

    private fun calculateRunData(){
        val distanceInMeters = 0
        for(polyline in pathPoints) {
            distanceInMeters += TrackingUtility.calculatePolylineLength(polyline).toInt()
        }


        val avgSpeed = Math.round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) / 10f

    }

    }
