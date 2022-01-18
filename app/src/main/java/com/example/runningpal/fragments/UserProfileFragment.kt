package com.example.runningpal.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.runningpal.activities.CameraActivity
import com.example.runningpal.R
import com.example.runningpal.db.User
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.others.Utils.convertBitmapToString
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.others.Utils.removeSharedPrefs
import com.example.runningpal.ui.adapters.ContactsAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import org.koin.android.ext.android.get
import timber.log.Timber


class UserProfileFragment : Fragment() {

    private lateinit var mAuth : FirebaseAuth
    private lateinit var viewModel : MainViewModel
    private lateinit var userViewModel : RunnersViewModel
    private lateinit var user : User
    private lateinit var contactsAdapter : ContactsAdapter
    private lateinit var userContacts : MutableLiveData<List<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_profile_menu, menu)}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = getUserSharedPrevs(requireContext())
        Timber.d("nick"+ user.nick)
        initSetup()
        setupRecyclerView()
        subscribeToObservers()
        setupUserProfile(user)

        ivUserProfileAvatar.setOnClickListener{ cameraShot() }

        btnUserProfileFindUser.setOnClickListener{ navHostFragment.findNavController().navigate(R.id.action_userProfileFragment_to_findUserFragment) }
    }

  fun  setupUserProfile(user: User){

      user.nick?.let{tvUserProfileName.setText(it)}

      Glide.with(this).load(R.drawable.default_user_avatar).into(ivUserProfileAvatar)

      Glide.with(this).load(R.drawable.default_user_background).into(ivUserProfileBackground)

      userContacts.postValue(user.contacts)
    }

    private fun subscribeToObservers(){

        userViewModel.getRunner(user.uid!!).observe(viewLifecycleOwner, Observer {
            Timber.d(" up + user "+ it.nick + "  "+ it.uid)
            userContacts.postValue(it.contacts) })

        userContacts.observe(viewLifecycleOwner, Observer {
            Timber.d(" up + user  obs")
            userViewModel.getSelectedRunners(it).observe(viewLifecycleOwner, Observer {

                it?.let {
                    contactsAdapter.submitList(it)
                    contactsAdapter.notifyDataSetChanged() }
            })

        })

        viewModel.getUserStatistics(user.uid!!).observe(viewLifecycleOwner , Observer {

            it.allDistance?.let{ tvUserProfileTotalDistanceInput.setText((it.toFloat() / 1000f).toString() + " km")}
            it.allCaloriesBurned?.let{ tvUserProfileTotalCaloriesBurnedInput.setText(it.toString())}
            it.allTime?.let{ tvUserProfileTotalRunningTimeInput.setText(TrackingUtility.getFormattedStopWatchTime(it,true))}
            it.allRuns?.let{ tvUserProfileRunAmountInput.setText(it.toString())}
        })
    }

    private fun initSetup(){
        mAuth = FirebaseAuth.getInstance()
        viewModel = get()
        userViewModel = get()
        userContacts = MutableLiveData()
    }

    private fun setupRecyclerView() = rvUserProvileFriends.apply {
        contactsAdapter = ContactsAdapter()
        adapter = contactsAdapter
        layoutManager = LinearLayoutManager(context)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_profile, container, false) }



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



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.user_profile_menu_logout -> {
                removeSharedPrefs(requireContext())
                mAuth.signOut()
                navHostFragment.findNavController().navigate(R.id.action_userProfileFragment_to_loginActivity)
                true
            }
            else -> {true}
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
