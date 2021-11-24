package com.example.runningpal

import android.content.ContentProvider
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.db.User
import com.example.runningpal.fragments.FriendProfileFragment
import com.example.runningpal.fragments.NewContactFragment
import com.example.runningpal.fragments.TrackingFragment
import com.example.runningpal.fragments.UserProfileFragment
import com.example.runningpal.others.Constants
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_find_contact.*
import kotlinx.android.synthetic.main.fragment_contacts.*
import kotlinx.android.synthetic.main.fragment_run.*
import org.koin.android.ext.android.get
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

               // viewModel.runner.postValue(User())

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