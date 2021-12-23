package com.example.runningpal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runningpal.R
import com.example.runningpal.db.Room
import com.example.runningpal.db.Runner
import com.example.runningpal.db.User
import com.example.runningpal.fragments.*
import com.example.runningpal.others.Constants
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.services.ListenerService
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*
import org.koin.android.ext.android.get
import timber.log.Timber

class DashboardActivity : AppCompatActivity() {


    companion object{ val runnersInRoom = MutableLiveData<User>() }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        mainViewModel = get()
        user = getUserSharedPrevs(this)

        Intent(baseContext, ListenerService::class.java).also { baseContext.startService(it) }

        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())

        navigateToTrackingFragmentIfNeeded(intent)

        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setDisplayShowHomeEnabled(true);
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        navigateToTrackingFragmentIfNeeded(intent)
    }

    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        // when ? tu powinno być when

        if(intent?.action == Constants.ACTION_SHOW_TRACKING_FRAGMENT) {

        }

        if(intent?.action == Constants.ACTION_SHOW_ROOM) {

            val roomID = intent.getStringExtra("IDROOM")
            val receiverID = intent.getStringExtra("idReceiver")

            val runner = Runner(user.uid,roomID,user.nick,user.profilePic,0f,0)
            mainViewModel.addRunnerToRoom(runner)
            
            Timber.d("niee jet null")

            RunRoomFragment.room = Room(roomID,null,false)
            navHostFragment.findNavController().navigate(R.id.action_runFragment_to_runRoomFragment)
        }
    }

}