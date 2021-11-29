package com.example.runningpal.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import kotlinx.android.synthetic.main.fragment_run_room.*
import org.koin.android.ext.android.get


class RunRoomFragment : Fragment() {

    private lateinit var  users : List<User>
    private lateinit var runnersViewModel : RunnersViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_room, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFriends()

       val dialog = ChooseUserDialogFragment()



        btnInviteRunRoomFragment.setOnClickListener {
            dialog.submitList(users)

            dialog.show(childFragmentManager, "NoticeDialogFragment")
        }


    }

    fun getFriends(){
        users = mutableListOf<User>()
        runnersViewModel = get()

        runnersViewModel.runners.observe(viewLifecycleOwner, Observer {
            users = mutableListOf<User>()
            val cont = it.contacts!!.toSet()

            runnersViewModel.getSelectedRunners(cont.toList()).observe(viewLifecycleOwner, Observer {

                users = it

            })


        })


    }





    }
