package com.example.runningpal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.runningpal.R
import com.example.runningpal.db.Runner
import com.example.runningpal.ui.adapters.RunnerCardAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_run_room_statistics.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.slidable_room_layout.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class RunRoomStatisticsFragment : Fragment() {

    private lateinit var runnersAdapter  : RunnerCardAdapter
    companion object {
        var runners = listOf<Runner>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run_room_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBarChart()
        setupRecyclerView()
        barChartOptions(runners)
        lineChartOptions(1)

        rgBarRoomChartOpt.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){

                R.id.rbFirst -> lineChartOptions(1)
                R.id.rbSecond -> lineChartOptions(2)
                R.id.rbThird -> lineChartOptions(3)
            }
        }

        btnBarChartRoom.setOnClickListener{
            runRoomBarChart.visibility = Button.VISIBLE
            runRoomLineChart.visibility = Button.GONE

        }

        btnLineChartRoom.setOnClickListener{
            runRoomBarChart.visibility = Button.GONE
            runRoomLineChart.visibility = Button.VISIBLE

        }


    }

    private fun barChartOptions(runs: List<Runner>) : BarDataSet{

        var barEntries = listOf<BarEntry>()
        var label = "Miejsca"


        barEntries = runs.indices.map { i ->
            BarEntry(
                i.toFloat(),
                runs[i].distanceMetres.toFloat()
            )}


        Timber.d(barEntries.size.toString())


        var bardataSet = BarDataSet(barEntries, label).apply {
            valueTextColor = Color.BLACK
            color = ContextCompat.getColor(requireContext(), R.color.colorAccent) }

        runRoomBarChart.data = BarData(bardataSet)
        runRoomBarChart.invalidate()

        return  bardataSet
    }

    private fun setupBarChart() {
        runRoomBarChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        runRoomBarChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        runRoomBarChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        runRoomBarChart.apply {
            legend.isEnabled = false
        }
    }


    private fun lineChartOptions(type: Int){
        var lineDataSets = ArrayList<ILineDataSet>()
        var  entriesList = ArrayList<ArrayList<Entry>>()

        when(type){

            1 -> {
                for (runner in runners) {
                    var i = 0
                    var entries = ArrayList<Entry>()

                    for (data in runner.distanceProcess!!) {
                        entries.add(
                            (Entry(
                                runner.timeProcess!![i].toFloat(),
                                runner.distanceProcess!![i].toFloat()
                            ))
                        )
                        i++
                    }

                    entriesList.add(entries)
                    var lineDataSet = LineDataSet(entries, runner.name)

                    var color = getRandomColor()
                    lineDataSet.color = color
                    lineDataSet.setCircleColor(color)

                    var bbol = lineDataSets.add(lineDataSet)
                    lineDataSet.valueTextSize = 12F
                    lineDataSet.lineWidth = 3f
                    runRoomLineChart.description.text = "Typ 1"



                }
            }

            2 -> {
                for (runner in runners) {
                    var i = 0
                    var entries = ArrayList<Entry>()
                    for (data in runner.distanceProcess!!) {
                        entries.add(
                            (Entry(
                                runner.timeProcess!![i].toFloat(),
                                runner.placeProcess!![i].toFloat()
                            ))
                        )
                        i++
                    }
                    var color = getRandomColor()

                    var lineDataSet = LineDataSet(entries, runner.name)
                    lineDataSet.color = color
                    lineDataSet.setCircleColor(color)
                    lineDataSets.add(lineDataSet)
                    lineDataSet.valueTextSize = 12F
                    lineDataSet.lineWidth = 3f
                    runRoomLineChart.description.text = "Typ 2"


                }
            }

            3 -> {
                for (runner in runners) {
                    var i = 0
                    var entries = ArrayList<Entry>()
                    for (data in runner.distanceProcess!!) {
                        entries.add(
                            (Entry(
                                runner.timeProcess!![i].toFloat(),
                                runner.avgSpeedProcess!![i]
                            ))
                        )
                        i++
                    }

                    var color = getRandomColor()
                    var lineDataSet = LineDataSet(entries, runner.name)

                    lineDataSet.color = color
                    lineDataSet.setCircleColor(color)
                    lineDataSets.add(lineDataSet)
                    lineDataSet.valueTextSize = 12F
                    lineDataSet.formSize = 10F
                    lineDataSet.setDrawFilled(true)
                    lineDataSet.circleRadius = 5F
                    lineDataSet.lineWidth = 3f
                    runRoomLineChart.description.text = "Typ 3"
                }
            }

        }


        var mainData = LineData(lineDataSets)


        runRoomLineChart.animateY(3000)
        runRoomLineChart.description.textSize = 16F

        runRoomLineChart.data = mainData
        runRoomLineChart.invalidate()

    }

    private fun setupRecyclerView() = rvRunRoomStatistics.apply {
        runnersAdapter = RunnerCardAdapter()
        runnersAdapter.submitList(runners)
        adapter = runnersAdapter
        layoutManager = LinearLayoutManager(requireContext()) }

    private fun getRandomColor() : Int{

        val rnd = Random()
        val color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        return color
    }
}