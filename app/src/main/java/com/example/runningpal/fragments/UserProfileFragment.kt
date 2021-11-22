package com.example.runningpal.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.DashboardActivity
import com.example.runningpal.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get


class UserProfileFragment : Fragment() {

    private  lateinit var mAuth : FirebaseAuth
    private lateinit var viewModel : StatisticsViewModel
    private lateinit var  currentUser : FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val currentUser =  mAuth.currentUser
        viewModel = get()

        var runStats = viewModel.getRunStatistics(currentUser?.uid!!).observe(viewLifecycleOwner, Observer {


        })

        btnUserProfileFriends.setOnClickListener{

            val intent = Intent(context, FindContactActivity::class.java)
            startActivity(intent)

        }


        tvUserProfileName.setText(currentUser?.displayName)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }


}