package com.example.runningpal.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.others.Constants
import com.example.runningpal.others.FiltrRunType
import com.example.runningpal.others.RunSortType
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.spDate
import kotlinx.android.synthetic.main.fragment_statistics.spFilter
import kotlinx.android.synthetic.main.fragment_statistics.spSortType
import org.koin.android.ext.android.get
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class RunFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private  lateinit var runAdapter : RunAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()
        requestPermissions()
        setupRecyclerView()



        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })


        btnNewRun.setOnClickListener{
            navHostFragment.findNavController().navigate(R.id.action_runFragment_to_trackingFragment2)
        }


        TrackingService.isTracking.value?.let{
            if(it) navHostFragment.findNavController().navigate(R.id.action_runFragment_to_trackingFragment2)
        }


        RunRoomFragment.isRoomCreated.value?.let{
            if(RunRoomFragment.isRoomCreated.value == true){navHostFragment.findNavController().navigate(
                R.id.action_runFragment_to_runRoomFragment
            )}
        }

        when(viewModel.sortType) {
            RunSortType.DATE -> spFilter.setSelection(0)
            RunSortType.RUNNING_TIME -> spFilter.setSelection(1)
            RunSortType.DISTANCE -> spFilter.setSelection(2)
            RunSortType.AVGSPEED -> spFilter.setSelection(3)
            RunSortType.CALORIESBURNED -> spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                when(pos) {
                    0 -> viewModel.sortRuns(RunSortType.DATE)
                    1 -> viewModel.sortRuns(RunSortType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(RunSortType.DISTANCE)
                    3 -> viewModel.sortRuns(RunSortType.AVGSPEED)
                    4 -> viewModel.sortRuns(RunSortType.CALORIESBURNED)
                }
            }
        }

        spSortType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                when(pos) {
                    0 -> viewModel.filtrRuns(FiltrRunType.INCREASE)
                    1 -> viewModel.filtrRuns(FiltrRunType.DECREASE)

                }
            }
        }

        spDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                pos: Int,
                id: Long
            ) {
                when(pos) {
                    0 -> return
                    1 -> viewModel.sortDateByThisWeek()
                    2 -> viewModel.sortDateByThisMonth()
                    3 -> viewModel.sortDateByLastMonth()
                }
            }
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
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {

            EasyPermissions.requestPermissions(
                this,
                "Papapass",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // ? dodaj sprawdzenie czy to działa , pokazuje w przypadku pemamently fuck up
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

}