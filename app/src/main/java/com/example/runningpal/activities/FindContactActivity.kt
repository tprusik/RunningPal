package com.example.runningpal.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.runningpal.R
import com.example.runningpal.fragments.FriendProfileFragment
import com.example.runningpal.fragments.NewContactFragment
import com.example.runningpal.others.Constants
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FindContactActivity : AppCompatActivity() {

    private lateinit var viewModel : RunnersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_contact)
       // viewModel = get()


        navigateToTrackingFragmentIfNeeded(getIntent())

        val findRunnerFragment = NewContactFragment()

        setCurrentFragment(findRunnerFragment)


    }

    override fun onResume() {
        super.onResume()
        navigateToTrackingFragmentIfNeeded(getIntent())

    }

    override fun onRestart() {
        super.onRestart()

        navigateToTrackingFragmentIfNeeded(getIntent())

    }
    override fun onPause() {
        super.onPause()

        navigateToTrackingFragmentIfNeeded(getIntent())

    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        navigateToTrackingFragmentIfNeeded(intent)
    }




    private fun navigateToTrackingFragmentIfNeeded(intent: Intent?) {
        if(intent?.action == Constants.ACTION_SHOW_FRIEND_PROFILE) {

            val profile = FriendProfileFragment()
            val id = intent.getStringExtra("id")

            val viewModel by viewModel<RunnersViewModel>()

            Timber.d("activ" + viewModel.hashCode())
            viewModel.getRunner(id!!).observe(this, Observer {

                viewModel.selectItem(it)

                Timber.d(" ssp" + it.nick)


            })
            setCurrentFragment(profile)


        }
    }


    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.profileContainer,fragment)
            commit()
        }


}