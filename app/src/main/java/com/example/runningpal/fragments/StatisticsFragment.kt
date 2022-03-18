package com.example.runningpal.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.runningpal.R
import com.example.runningpal.db.Run
import com.example.runningpal.others.ChartType
import com.example.runningpal.others.CustomMarkerView
import com.example.runningpal.others.FiltrRunType
import com.example.runningpal.others.RunSortType
import com.example.runningpal.ui.viewmodels.MainViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_run.spDate
import kotlinx.android.synthetic.main.fragment_run.spFilter
import kotlinx.android.synthetic.main.fragment_run.spSortType
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.sliding_layout.*
import org.koin.android.ext.android.get
import timber.log.Timber


class StatisticsFragment : Fragment()  {


    private lateinit var viewModel : MainViewModel

    private val charts = arrayOfNulls<LineChart>(4)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = get()


       ////
        setupBarChart()
        ////

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            it?.let {

                barChartOptions(it)
                pieChartOptions(it)
                lineChartOptions(it)
            }

            btnBarChart.setOnClickListener{
                barChart.visibility = Button.VISIBLE
                pieChart.visibility = Button.GONE
                lineChart.visibility = Button.GONE
            }

            btnPieChart.setOnClickListener{

                pieChart.visibility = Button.VISIBLE
                lineChart.visibility = Button.GONE
                barChart.visibility = Button.GONE

            }

        })


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
                    0 -> applyOptions()
                    1 -> viewModel.sortDateByThisWeek()
                    2 -> viewModel.sortDateByThisMonth()
                    3 -> viewModel.sortDateByLastMonth()
                }
            }
        }


        btn_slidable_apply.setOnClickListener {
           applyOptions()}

        btn_slidable_reset.setOnClickListener {
            resetFilterOptions()
            resetFilterOptionsInfo()}

        rgBarChartOpt.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){

                R.id.rbFirst -> viewModel.runs.value?.let {
                    barChartOptions(it)
                    pieChartOptions(it)
                    lineChartOptions(it) }
                R.id.rbSecond -> viewModel.runs.value?.let {
                    barChartOptions(it)
                    pieChartOptions(it)
                    lineChartOptions(it) }

                R.id.rbThird -> viewModel.runs.value?.let {
                    barChartOptions(it)
                    pieChartOptions(it)
                    lineChartOptions(it) }
            }
        }

    }

    private fun applyOptions(){
        resetFilterOptions()
        applySortOptions()
        setFiltersOptionsInfo()
    }

    private fun setupBarChart() {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textSize = 12f
            textColor = Color.BLACK
            setDrawGridLines(false)
        }
        barChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            textSize = 12f
            setDrawGridLines(false)
        }
        barChart.apply {
            legend.isEnabled = false
        }
    }

    private fun applySortOptions(){

        if(! et_slidable_sort_calories_greater.text.isNullOrBlank())
            viewModel.sortRunByCaloriesGreaterThan(
                Integer.parseInt(
                    et_slidable_sort_calories_greater.text.toString()
                )
            )

        if(! et_slidable_sort_calories_smaller.text.isNullOrBlank())
            viewModel.sortRunByCaloriesSmallerThan(
                Integer.parseInt(
                    et_slidable_sort_calories_smaller.text.toString()
                )
            )

        if(! et_slidable_sort_date_from.text.isNullOrBlank())
            viewModel.sortRunByDateGreaterThan(et_slidable_sort_date_from.text.toString())

        if(! et_slidable_sort_date_to.text.isNullOrBlank())
            viewModel.sortRunByDateSmallerThan(et_slidable_sort_date_to.text.toString())

        if(! et_slidable_sort_distance_greater.text.isNullOrBlank())
            viewModel.sortRunByDistanceGreaterThan(
                et_slidable_sort_distance_greater.text.toString().toLong()
            )

        if(! et_slidable_sort_distance_smaller.text.isNullOrBlank())
            viewModel.sortRunByDistanceSmallerThan(
                et_slidable_sort_distance_smaller.text.toString().toLong()
            )

        if(! et_slidable_sort_speed_greater.text.isNullOrBlank())
            viewModel.sortRunBySpeedGreaterThan(Integer.parseInt(et_slidable_sort_speed_greater.text.toString()))

        if(! et_slidable_sort_speed_smaller.text.isNullOrBlank())
            viewModel.sortRunBySpeedSmallerThan(Integer.parseInt(et_slidable_sort_speed_smaller.text.toString()))

        if(! et_slidable_sort_time_greater.text.isNullOrBlank())
            viewModel.sortRunByTimeGreaterThan(
                et_slidable_sort_time_greater.text.toString().toLong()
            )

        if(! et_slidable_sort_time_smaller.text.isNullOrBlank())
            viewModel.sortRunByTimeSmallerThan(
                et_slidable_sort_time_smaller.text.toString().toLong()
            )
    }

    private fun setFiltersOptionsInfo(){

        var filtrOptions = ""

        if(! et_slidable_sort_calories_greater.text.isNullOrBlank())
            filtrOptions += " kalorie > " + et_slidable_sort_calories_greater.text.toString()

        if(! et_slidable_sort_calories_smaller.text.isNullOrBlank())
            filtrOptions += " kalorie < " + et_slidable_sort_calories_smaller.text.toString()

        if(! et_slidable_sort_date_from.text.isNullOrBlank())
            filtrOptions += " data od: " + et_slidable_sort_date_from.text.toString()

        if(! et_slidable_sort_date_to.text.isNullOrBlank())
            filtrOptions += " data do: " + et_slidable_sort_date_to.text.toString()

        if(! et_slidable_sort_distance_greater.text.isNullOrBlank())
            filtrOptions += " dystans > " + et_slidable_sort_distance_greater.text.toString()

        if(! et_slidable_sort_distance_smaller.text.isNullOrBlank())
            filtrOptions += " dystans < " + et_slidable_sort_distance_smaller.text.toString()

        if(! et_slidable_sort_speed_greater.text.isNullOrBlank())
            filtrOptions += " prędkość > " + et_slidable_sort_speed_greater.text.toString()

        if(! et_slidable_sort_speed_smaller.text.isNullOrBlank())
            filtrOptions += " prędkość < " + et_slidable_sort_speed_smaller.text.toString()

        if(! et_slidable_sort_time_greater.text.isNullOrBlank())
            filtrOptions += " czas > " + et_slidable_sort_time_greater.text.toString()

        if(! et_slidable_sort_time_smaller.text.isNullOrBlank())
            filtrOptions += " czas < " + et_slidable_sort_time_smaller.text.toString()

        tvSlidableLayoutFilters.text = ("Filtry: " + filtrOptions)

    }

    private fun resetFilterOptionsInfo(){
        tvSlidableLayoutFilters.text = "Filtry: "

        et_slidable_sort_calories_greater.text.clear()
        et_slidable_sort_calories_smaller.text.clear()
        et_slidable_sort_date_from.text.clear()
        et_slidable_sort_date_to.text.clear()
        et_slidable_sort_distance_greater.text.clear()
        et_slidable_sort_distance_smaller.text.clear()
        et_slidable_sort_speed_greater.text.clear()
        et_slidable_sort_speed_smaller.text.clear()
        et_slidable_sort_time_greater.text.clear()
        et_slidable_sort_time_smaller.text.clear()
    }

    private fun resetFilterOptions(){

        var pos= spFilter.selectedItemPosition

        when(pos){
            0 -> viewModel.sortRuns(RunSortType.DATE)
            1 -> viewModel.sortRuns(RunSortType.RUNNING_TIME)
            2 -> viewModel.sortRuns(RunSortType.DISTANCE)
            3 -> viewModel.sortRuns(RunSortType.AVGSPEED)
            4 -> viewModel.sortRuns(RunSortType.CALORIESBURNED)
        }

        barChart.zoomOut()

    }

    private fun barChartOptions(runs: List<Run>) : BarDataSet{

        var barEntries = listOf<BarEntry>()
        var label = ""

        when(rgBarChartOpt.checkedRadioButtonId) {
            R.id.rbFirst -> {
                barChart.description.text = "Tempo(Czas)"
                pieChart.description.text = "Tempo(Czas)"
                lineChart.description.text = "Tempo(Czas)" }
            R.id.rbSecond ->{
                barChart.description.text = "Kalorie(Czas)"
                pieChart.description.text = "Kalorie(Czas)"
                lineChart.description.text = "Kalorie(Czas)" }

            R.id.rbThird -> {
                barChart.description.text = "Prędkość(Czas)"
                pieChart.description.text = "Prędkość(Czas)"
                lineChart.description.text = "Prędkość(Czas)" }
        }

            when(rgBarChartOpt.checkedRadioButtonId) {
                R.id.rbFirst -> barEntries = runs.indices.map { i -> BarEntry(
                                i.toFloat(),
                                runs[i].avgSpeedKmh) }

                R.id.rbSecond -> barEntries = runs.indices.map { i ->
                    BarEntry(
                            i.toFloat(),
                            runs[i].caloriesBurned.toFloat()) }

                R.id.rbThird -> barEntries = runs.indices.map { i ->
                    BarEntry(
                            i.toFloat(),
                            runs[i].distanceMetres.toFloat()) }
            }


        Timber.d(barEntries.size.toString())
        Timber.d(runs.size.toString())

       var bardataSet = BarDataSet(barEntries, label).apply {
            valueTextColor = Color.BLACK
            valueTextSize = 14F
            color = ContextCompat.getColor(requireContext(), R.color.colorAccent) }

        bardataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
        barChart.animateY(3000)
        barChart.description.textSize = 16F
        barChart.data = BarData(bardataSet)
        barChart.marker = CustomMarkerView(ChartType.BarChart,runs, requireContext(), R.layout.marker_view)
        barChart.invalidate()
        return  bardataSet
    }

    private fun pieChartOptions(runs: List<Run>){

        var pieEntries = listOf<PieEntry>()

        when(rgBarChartOpt.checkedRadioButtonId) {
            R.id.rbFirst -> pieEntries = runs.indices.map { i ->
                PieEntry(runs[i].avgSpeedKmh) }

            R.id.rbSecond -> pieEntries = runs.indices.map { i ->
                PieEntry(runs[i].caloriesBurned.toFloat()) }

            R.id.rbThird -> pieEntries = runs.indices.map { i ->
                PieEntry(runs[i].distanceMetres.toFloat()) }
        }

        val pieDataSet = PieDataSet(pieEntries, "").apply {
            valueTextColor = Color.BLACK
            valueTextSize = 14F
        }

        pieChart.animateY(3000)
        pieChart.description.textSize = 16F
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
        pieChart.data = PieData(pieDataSet)

        pieChart.marker = CustomMarkerView(
                ChartType.PieChart,
                runs,
                requireContext(),
                R.layout.marker_view)

        pieChart.invalidate()
    }

    private fun lineChartOptions(runs: List<Run>){


        val allAvgSpeedsPie =
                runs.indices.map { i -> PieEntry(i.toFloat(), runs[i].avgSpeedKmh) }

        val lineDataSet = LineDataSet(allAvgSpeedsPie, "Avg Speed Over Time").apply {
            valueTextColor = Color.BLACK
        }

        lineDataSet.setColors(ColorTemplate.COLORFUL_COLORS.toMutableList())
        lineChart.data = LineData(lineDataSet)
        lineChart.marker = CustomMarkerView(
                ChartType.LineChart,
                runs.reversed(),
                requireContext(),
                R.layout.marker_view
        )

    }


}