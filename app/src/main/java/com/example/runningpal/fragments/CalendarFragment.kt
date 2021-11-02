package com.example.runningpal.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.runningpal.CalendarAdapter
import com.example.runningpal.R
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarFragment :  Fragment(), CalendarAdapter.OnItemListener {


    private  lateinit var selectedDate : LocalDate
   // private lateinit var monthYearText : TextView
    //private lateinit var calendarRecyclerView : RecyclerView



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectedDate = LocalDate.now()
        setMonthView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setMonthView() {

       tvCalendarMonth.setText(monthYearFromDate(selectedDate))

        val daysOfMonth = daysInMonthArray(selectedDate)

        val daysOfMon = ArrayList < String>()

        val calendarAdapter = CalendarAdapter(daysOfMonth,this)

        val layoutManager =GridLayoutManager(context,7)

        rvCalendarCell.adapter = calendarAdapter
        rvCalendarCell.layoutManager = layoutManager

        btnCalendarNext.setOnClickListener{

            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }

        btnCalendarPrevious.setOnClickListener{

            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun daysInMonthArray(date:LocalDate): ArrayList<String>{

         val daysInMonthArr = ArrayList<String>()
         val yearMonth = YearMonth.from(date)

        val daysInMonth = yearMonth.lengthOfMonth()
        val firstOfMonth = selectedDate.withDayOfMonth(1)

        val dayOfWeek = firstOfMonth.dayOfWeek.value

        for (i in 1..42) {

            if(i<= dayOfWeek || i > daysInMonth + dayOfWeek)
            {daysInMonthArr.add("")}
            else{
                daysInMonthArr.add((i-dayOfWeek).toString())
            }

        }
        return daysInMonthArr

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun monthYearFromDate(date:LocalDate) : String {

        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onItemClick(position: Int, dayText: String?) {

                Toast.makeText(context,"wcisnałeś dzień" +  dayText,Toast.LENGTH_SHORT).show()

    }


}


