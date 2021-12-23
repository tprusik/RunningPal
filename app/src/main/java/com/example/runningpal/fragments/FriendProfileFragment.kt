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
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import kotlinx.android.synthetic.main.fragment_friend_profile.*
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class FriendProfileFragment : Fragment() {

    private lateinit var viewModel : StatisticsViewModel
    private lateinit var userViewModel : RunnersViewModel
    private lateinit var user : User
    private lateinit var friendObject : User

    companion object{ val friend = MutableLiveData<User>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = get()
        userViewModel = get()
        user = getUserSharedPrevs(requireContext())


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



        fabFriendProfileAdd.setOnClickListener {

            //val user = friendViewModel.user.value!!

               // user.contacts!!.add(runner.uid!!)

                Timber.d("nick" + friendObject.nick)

                friendViewModel.updateUser(user)


                var friendInvitation = FriendInvitation(user.uid, friendObject.uid)
                userViewModel.sendFriendInvitation(friendInvitation)

                Toast.makeText(context,"dodano kolegę do bazy",Toast.LENGTH_SHORT).show()
        }

        btnFriendProfile.setOnClickListener {}
    }


}