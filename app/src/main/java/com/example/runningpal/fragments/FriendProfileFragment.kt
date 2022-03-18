package com.example.runningpal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.runningpal.R
import com.example.runningpal.db.FriendInvitation
import com.example.runningpal.db.User
import com.example.runningpal.others.Utils
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import kotlinx.android.synthetic.main.fragment_friend_profile.tvFriendProfileRunAmountInput

import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.sql.Time


class FriendProfileFragment : Fragment() {

    private lateinit var viewModel : MainViewModel
    private lateinit var userViewModel : RunnersViewModel
    private lateinit var user : User
    private lateinit var friendObject : User

    private var isAlreadyFriend = false

    companion object{ val friend = MutableLiveData<User>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_profile, container, false) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = get()
        userViewModel = get()
        user = getUserSharedPrevs(requireContext())

        subscribeToObservers()
        val friendViewModel by sharedViewModel<RunnersViewModel>()


        Timber.d("onViewCreated fragment" + friendViewModel.hashCode())

      friend.observe(viewLifecycleOwner, Observer {

          friendObject = it

            tvFriendProfileName.setText(it.nick)

            if (it.profilePic == null) {
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivFriendProfileAvatar)
            } else {
                val pic = Utils.convertStringToBitmap(it.profilePic!!)
                Glide.with(this).load(pic).into(ivFriendProfileAvatar)

            }

            if (it.backgroundPic == null) {
                Glide.with(this).load(R.drawable.default_user_background).into(ivFriendProfileBackground)

            } else {
                val pic = Utils.convertStringToBitmap(it.backgroundPic!!)
                Glide.with(this).load(pic).into(ivFriendProfileAvatar)
            }

        })



        changeButtonVisibility()

        fabFriendProfileAdd.setOnClickListener {

                Timber.d("nick" + friendObject.nick)
                friendViewModel.updateUser(user)

                var friendInvitation = FriendInvitation(user.uid, friendObject.uid)
                userViewModel.sendFriendInvitation(friendInvitation)

        }


    }

    fun changeButtonVisibility(){

        if(isAlreadyFriend)
            fabFriendProfileAdd.visibility = FloatingActionButton.GONE
    }

    fun subscribeToObservers(){


        friend.value?.uid?.let {
            viewModel.getUserStatistics(it).observe(viewLifecycleOwner , Observer {

                Timber.d("id  "+ friend.value!!.uid!!)
                it.allDistance?.let{ tvFriendProfileRunAmountInput.setText(it.toString())}
                it.allCaloriesBurned?.let{ tvFriendProfileTotalCaloriesBurnedInput.setText(it.toString())}
                it.allTime?.let{ tvFriendProfileTotalRunningTimeInput.setText(it.toString())}
                it.allRuns?.let{ tvFriendProfileRunAmountInput.setText(it.toString())}
            })
        }

        userViewModel.user.observe(viewLifecycleOwner, Observer {

            it.contacts?.let{
                for(friends in it)
                { if(friend.value?.uid == friends){
                    isAlreadyFriend = true
                    changeButtonVisibility() } } }
        })

    }

}