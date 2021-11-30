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
import com.example.runningpal.db.Invitation
import com.example.runningpal.db.User
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.get
import timber.log.Timber


class ChooseUserDialogFragment :  DialogFragment() {

    private lateinit var users : List<User>
    private lateinit var nicks : MutableList<String>
    private lateinit var usersAray : Array<String>
    private lateinit var viewModel: RunnersViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {

            val builder = AlertDialog.Builder(it)
           builder.setItems(usersAray,DialogInterface.OnClickListener { dialog, which ->

            sendInvite(which)

           })

            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun sendInvite(id : Int){

        val user = users.get(id)

        val invitation = Invitation(user.uid,"rVZAsWtKgpeAHaeIN8vTlxSdcpk1","1")

        viewModel.sendInvitation(invitation)
        Timber.d("Wysłano zaproszenie")

    }

    init{

        nicks = mutableListOf<String>()
        usersAray = arrayOf<String>()
        viewModel = get()

    }

    fun submitList(user : List<User>){

        users = user


        for(u in user){

            Timber.d("dodano")
            nicks.add(u.nick!!)

        }

        usersAray = nicks.toTypedArray() as Array<String>

    }


}
