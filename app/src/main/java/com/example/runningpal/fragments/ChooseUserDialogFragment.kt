package com.example.runningpal.fragments

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import org.koin.android.ext.android.get
import timber.log.Timber


class ChooseUserDialogFragment :  DialogFragment() {

    private lateinit var users : List<User>
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)
            builder.setMessage(users[0].nick)
                .setPositiveButton("R.string.fire",
                    DialogInterface.OnClickListener { dialog, id ->
                        // FIRE ZE MISSILES!
                    })
                .setNegativeButton("Wylacz",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun submitList(user : List<User>){
        users = user
    }


}
