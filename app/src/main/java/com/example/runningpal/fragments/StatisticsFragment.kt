package com.example.runningpal.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.runningpal.R
import com.example.runningpal.db.RunStatistics
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.example.runningpal.ui.viewmodels.StatisticsViewModel
import kotlinx.android.synthetic.main.fragment_statistics.*
import org.koin.android.ext.android.get

class StatisticsFragment : Fragment() {

    private lateinit var viewModel :  MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()

        viewModel.totalDistance.observe(viewLifecycleOwner , Observer {

            var totalDistance = it

            tvTotalDistance.setText(createStatistics(it))

        })

    }

    private fun createStatistics(runStatistics : RunStatistics) : String {

        return " Ilosc biegów : \n ${runStatistics.allRuns} \n Przebyty dystans : \n ${runStatistics.allDistance} \n Spalone kalorie : \n ${runStatistics.allCaloriesBurned} "

    }

}