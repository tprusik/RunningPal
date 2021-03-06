package com.example.runningpal.others

import android.content.Context
import com.example.runningpal.db.Run
import com.example.runningpal.db.Runner
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.fragment_statistics.view.*
import kotlinx.android.synthetic.main.marker_view.view.*
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView(
        val chartType:ChartType,
        val runs: List<Run>,
        c: Context,
        layoutId: Int
) : MarkerView(c, layoutId) {


    override fun getOffset(): MPPointF {
        return MPPointF(-width / 2f, -height.toFloat()) }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if (e == null) {
            return }

        var curRunId = 0;

        when (chartType) {

            ChartType.BarChart -> curRunId = e.x.toInt()
            ChartType.PieChart -> curRunId = e.x.toInt()
            ChartType.LineChart -> return
        }

        val run = runs[curRunId]

        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timestamp
        }

        val dateFormat = SimpleDateFormat("dd.MM.yy", Locale.getDefault())
        tvDate.text = dateFormat.format(calendar.time)

        val avgSpeed = "${run.avgSpeedKmh}km/h"
        tvAvgSpeed.text = avgSpeed

        val distanceInKm = "${run.distanceMetres / 1000f}km"
        tvDistance.text = distanceInKm

        tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMilis)

        val caloriesBurned = "${run.caloriesBurned}kcal"
        tvCaloriesBurned.text = caloriesBurned
    }


    private fun findID(id: Int): Int {
        var runID = -1
        for (run in runs) {
            if (run.caloriesBurned == id)
                return runID
            else
                runID++
        }

        return runID

    }
    }


