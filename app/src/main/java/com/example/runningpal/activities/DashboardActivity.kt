package com.example.runningpal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.fragments.*
import com.example.runningpal.others.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {


    companion object{

        val runnersInRoom = MutableLiveData<User>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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

            val id = intent.getStringExtra("idRoom")
            val receiverID = intent.getStringExtra("idReceiver")


        }
    }


}