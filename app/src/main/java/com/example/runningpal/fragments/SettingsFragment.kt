package com.example.runningpal.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.runningpal.R
import com.example.runningpal.others.Constants
import kotlinx.android.synthetic.main.fragment_setup.*



class SettingsFragment : Fragment() {

    lateinit var sharedPref : SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState) }


    private fun changePersonalDataToSharedPref(): Boolean {
        val name = etName.text.toString()
        val weight = etWeight.text.toString()
        if(name.isEmpty() || weight.isEmpty()) {
            return false
        }
        sharedPref.edit()
            .putString(Constants.KEY_USER_NAME, name)
            .putFloat(Constants.KEY_USER_WEIGHT, weight.toFloat())
            .apply()
        return true
    }



}