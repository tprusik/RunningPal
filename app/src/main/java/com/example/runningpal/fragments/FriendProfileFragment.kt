package com.example.runningpal.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.runningpal.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.DatabaseUtility
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class FriendProfileFragment : Fragment() {

    private lateinit var viewModel : StatisticsViewModel


    private lateinit var runner : User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_profile, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = get()


        val friendViewModel by sharedViewModel<RunnersViewModel>()


        Timber.d("onViewCreated fragment" + friendViewModel.hashCode())
        friendViewModel.selectedItem.observe(viewLifecycleOwner, Observer {

            runner = it

            tvFriendProfileName.setText(it.nick)


            if (it.profilePic == null) {
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivFriendProfileAvatar)
            } else {
                val pic = DatabaseUtility.convertStringToBitmap(it.profilePic!!)
                Glide.with(this).load(pic).into(ivFriendProfileAvatar)

            }

            if (it.backgroundPic == null) {
                Glide.with(this).load(R.drawable.default_user_background).into(ivFriendProfileBackground)

            } else {
                val pic = DatabaseUtility.convertStringToBitmap(it.backgroundPic!!)
                Glide.with(this).load(pic).into(ivFriendProfileAvatar)
            }

        })



        fabFriendProfileAdd.setOnClickListener {

            val user = friendViewModel.user.value!!

                user.contacts!!.add(runner.uid!!)

                Timber.d("nick" + runner.nick)

                friendViewModel.updateUser(user)

                Toast.makeText(context,"dodano kolegę do bazy",Toast.LENGTH_SHORT).show()

        }
    }




}