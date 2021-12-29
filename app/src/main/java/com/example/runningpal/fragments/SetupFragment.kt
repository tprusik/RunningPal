package com.example.runningpal.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.KEY_FIRST_TIME_IN_APP
import com.example.runningpal.others.Constants.KEY_USER_NAME
import com.example.runningpal.others.Constants.KEY_USER_WEIGHT
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_setup.*
import org.koin.android.ext.android.get


class SetupFragment : Fragment() {


    lateinit var user : User
    lateinit var viewModel : RunnersViewModel

    // tutaj injection
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = getUserSharedPrevs(requireContext())
        viewModel = get()

        tvSetupContinue.setOnClickListener {
           val userWeight =  etSetupWeight.text

            user.weight = userWeight.toString()

            viewModel.updateUser(user)
        }
    }

}