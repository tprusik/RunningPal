package com.example.runningpal.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runningpal.R
import com.example.runningpal.others.Constants.KEY_FIRST_TIME_IN_APP
import com.example.runningpal.others.Constants.KEY_USER_NAME
import com.example.runningpal.others.Constants.KEY_USER_WEIGHT
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_setup.*


class SetupFragment : Fragment() {


    lateinit var sharedPref: SharedPreferences

    // tutaj injection
    var isFirstAppOpen = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
/**
        if(!isFirstAppOpen) {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.setupFragment, true)
                .build()
            findNavController().navigate(
                R.id.action_setupFragment_to_runFragment,
                savedInstanceState,
                navOptions
            )
        }
        **/

        tvContinue.setOnClickListener {
            val success = writePersonalDataToSharedPref()
            if(success) {
               // findNavController().navigate(R.id.action_setupFragment_to_runFragment)
            } else {
                Snackbar.make(requireView(), "Please enter all the fields", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun writePersonalDataToSharedPref(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(KEY_USER_NAME, name)
            .putFloat(KEY_USER_WEIGHT, weight.toFloat())
            .putBoolean(KEY_FIRST_TIME_IN_APP, false)
            .apply()
        return true
    }
}