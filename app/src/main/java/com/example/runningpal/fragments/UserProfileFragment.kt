package com.example.runningpal.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningpal.CameraActivity
import com.example.runningpal.DashboardActivity
import com.example.runningpal.FindContactActivity
import com.example.runningpal.R
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.db.User
import com.example.runningpal.others.DatabaseUtility.convertBitmapToString
import com.example.runningpal.others.DatabaseUtility.convertStringToBitmap
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get
import java.io.IOException


class UserProfileFragment : Fragment() {

    private  lateinit var mAuth : FirebaseAuth
    private lateinit var statisticsViewModel : StatisticsViewModel
    private lateinit var userViewModel : RunnersViewModel
    private lateinit var  currentUser : FirebaseUser
    private lateinit var user : User
    private lateinit var contactsAdapter : ContactsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        val currentUser =  mAuth.currentUser
        statisticsViewModel = get()
        userViewModel = get()

        setupRecyclerView()




        userViewModel.user.observe(viewLifecycleOwner, Observer {

            user = it

            tvUserProfileName.setText(it.nick)
            tvUserProfileEmail.setText(it.email)

            if(it.profilePic==null){
                Glide.with(this).load(R.drawable.default_user_avatar).into(ivUserProfileAvatar)
            }
            else
            {
                val pic = convertStringToBitmap(it.profilePic!!)
                Glide.with(this).load(pic).into(ivUserProfileAvatar)

            }

            if(it.backgroundPic==null){
                Glide.with(this).load(R.drawable.default_user_background).into(ivUserProfileBackground)

            }
            else
            {
                val pic = convertStringToBitmap(it.backgroundPic!!)
                Glide.with(this).load(pic).into(ivUserProfileAvatar)
            }

            val ids = it.contacts as List<String>
            userViewModel.getSelectedRunner(ids).observe(viewLifecycleOwner, Observer {

                contactsAdapter.submitList(it)


            })




        })


        ivUserProfileAvatar.setOnClickListener{

            cameraShot()

        }

        btnUserProfileFriends.setOnClickListener{

            val intent = Intent(context,FindContactActivity::class.java)
            startActivity(intent)
        }


        tvUserProfileName.setText(currentUser?.displayName)
    }

    private fun setupRecyclerView() = rvUserProvileFriends.apply {
        contactsAdapter = ContactsAdapter()
        adapter = contactsAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK) {
            if (requestCode == CameraActivity.CAMERA_REQUEST_CODE) {
                val snapshot: Bitmap = data!!.extras!!.get("data") as Bitmap

                ivUserProfileAvatar.setImageBitmap(snapshot)

                user.profilePic = convertBitmapToString(snapshot)

                userViewModel.updateUser(user)

            }
        }
    }

    fun cameraShot() {
            if (ContextCompat.checkSelfPermission(
                           requireContext(),
                            Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CameraActivity.CAMERA_REQUEST_CODE)

            } else {
                ActivityCompat.requestPermissions(requireActivity(),
                        arrayOf(Manifest.permission.CAMERA),
                        CameraActivity.CAMERA_PERMISSION_CODE)
            }
        }
    }
