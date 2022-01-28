package com.example.runningpal.fragments

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.others.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.example.runningpal.others.CustomMarkerView
import com.example.runningpal.others.FiltrRunType
import com.example.runningpal.others.RunSortType
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.services.TrackingService
import com.example.runningpal.ui.adapters.RunAdapter
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.fragment_run.*
import org.koin.android.ext.android.get
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class RunFragment : Fragment() , EasyPermissions.PermissionCallbacks {

    private  lateinit var runAdapter : RunAdapter
    private lateinit var viewModel : MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)

    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()
        requestPermissions()
        setupRecyclerView()

       ////
        setupBarChart()

        ////

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            it?.let {

                val allAvgSpeedsBar = it.indices.map { i -> BarEntry(i.toFloat(), it[i].avgSpeedKmh) }
                val bardataSet = BarDataSet(allAvgSpeedsBar, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE
                    color = ContextCompat.getColor(requireContext(), R.color.colorAccent) }

                val allAvgSpeedsPie = it.indices.map { i -> PieEntry(i.toFloat(), it[i].avgSpeedKmh) }
                val pieDataSet = PieDataSet(allAvgSpeedsPie, "Avg Speed Over Time").apply {
                    valueTextColor = Color.WHITE }


                barChart.data = BarData(bardataSet)
                barChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                barChart.invalidate()

                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
                pieChart.data = PieData(pieDataSet)
                pieChart.marker = CustomMarkerView(it.reversed(), requireContext(), R.layout.marker_view)
                pieChart.invalidate()
            }
        })


        RunRoomFragment.isRoomCreated.value?.let{
            if(RunRoomFragment.isRoomCreated.value == true){navHostFragment.findNavController().navigate(
                R.id.action_runFragment_to_runRoomFragment
            )}
        }



        TrackingService.isTracking.value?.let{
            if(it) navHostFragment.findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
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

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        barChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }


}