package com.example.runningpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.fragment.app.Fragment
import com.example.runningpal.fragments.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    private  lateinit var mAuth : FirebaseAuth
    private  lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        mAuth = FirebaseAuth.getInstance()
        // niekonieczne to co poniżej


        // change striung resources
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.common_open_on_phone,R.string.appbar_scrolling_view_behavior)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        slidable_nav.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.slidableMenuItem1 -> Toast.makeText(applicationContext, "item 1", Toast.LENGTH_SHORT).show()

                R.id.slidableMenuLogout -> {

                    mAuth.signOut()

                    val intent = Intent(this, RegisterActivity::class.java)
                    startActivity(intent)
                  finish()



                }

            }
            true
        }


        val calendarFragment = CalendarFragment()
        val mapFragment = TrackingFragment()
        val userProfileFragment = UserProfileFragment()
        val messageFragment = MessageFragment()
        val contactsFragment = ContactsFragment()

        setCurrentFragment(calendarFragment)

        // pasek nawigacji
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {

                R.id.mi_friends -> {

                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fConainer, contactsFragment)
                        commit()
                    }

                }

                R.id.mi_calendar -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fConainer, calendarFragment)
                        commit()
                    }
                }


                R.id.mi_sportsActivity -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fConainer, mapFragment)
                        commit()
                    }
                }


                R.id.mi_profile -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fConainer, userProfileFragment)
                        commit()
                    }
                }


                R.id.mi_messages -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fConainer, messageFragment)
                        commit()
                    }

                }
            }
            true
        }


    }

    private fun setCurrentFragment(fragment:Fragment) =
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fConainer,fragment)
                commit()
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


}