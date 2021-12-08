package com.example.runningpal.fragments

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.others.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.runningpal.others.RunSortType
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.ListenerService
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_run.*
import org.koin.android.ext.android.get
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber

class RunFragment : Fragment() , EasyPermissions.PermissionCallbacks {

    private  lateinit var runAdapter : RunAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()
        requestPermissions()
        setupRecyclerView()


        when(viewModel.sortType) {
            RunSortType.DATE -> spFilter.setSelection(0)
            RunSortType.RUNNING_TIME -> spFilter.setSelection(1)
            RunSortType.DISTANCE -> spFilter.setSelection(2)
            RunSortType.AVGSPEED -> spFilter.setSelection(3)
            RunSortType.CALORIESBURNED -> spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos) {
                    0 -> viewModel.sortRuns(RunSortType.DATE)
                    1 -> viewModel.sortRuns(RunSortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(RunSortType.DISTANCE)
                    3 -> viewModel.sortRuns(RunSortType.AVGSPEED)
                    4 -> viewModel.sortRuns(RunSortType.CALORIESBURNED)
                }
            }
        }


        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })



        btnNewRun.setOnClickListener{
            navHostFragment.findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
        }

    }

    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }


    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                    this,
                    "Papaapa",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {

            EasyPermissions.requestPermissions(
                    this,
                    "Papapass",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // ? dodaj sprawdzenie czy to działa , pokazuje w przypadku pemamently fuck up
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}