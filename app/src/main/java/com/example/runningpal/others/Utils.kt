package com.example.runningpal.others

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants.KEY_USER_AVATAR
import com.example.runningpal.others.Constants.KEY_USER_BCK
import com.example.runningpal.others.Constants.KEY_USER_EMAIL
import com.example.runningpal.others.Constants.KEY_USER_ID
import com.example.runningpal.others.Constants.KEY_USER_NAME
import com.example.runningpal.others.Constants.KEY_USER_WEIGHT
import com.example.runningpal.others.Constants.SHARED_PREFERENCES_NAME
import com.github.mikephil.charting.data.BarEntry
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object Utils {

    fun convertStringToBitmap(pic: String) : Bitmap{

        val decodedString: ByteArray = Base64.decode(pic, Base64.URL_SAFE)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size) }

   fun convertBitmapToString(bmp: Bitmap) : String{

         val bitmap = bmp!!.copy(bmp.config, true)

            val bao = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao) // bmp is bitmap from user image file
            bitmap.recycle()
            val byteArray: ByteArray = bao.toByteArray()
            val imageB64: String = Base64.encodeToString(byteArray, Base64.URL_SAFE)

            return imageB64

    }

    fun getUserSharedPrevs(context: Context) : User {

        val sharedPreferences: SharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        val name = sharedPreferences.getString(KEY_USER_NAME, "BLAD")
        val email = sharedPreferences.getString(KEY_USER_EMAIL, null)
        val weight = sharedPreferences.getString(KEY_USER_WEIGHT, null)
        val id = sharedPreferences.getString(KEY_USER_ID, "0")
        val avatar = sharedPreferences.getString(KEY_USER_AVATAR, null)
        val background = sharedPreferences.getString(KEY_USER_BCK, null)


        val user = User().also {
            it.nick = name
            it.uid = id
            it.profilePic = avatar
            it.backgroundPic = background
            it.weight = weight
            it.email = email}

        return user
    }

    fun removeSharedPrefs(context: Context) {

        val sharedPreferences: SharedPreferences.Editor = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        sharedPreferences.clear()
        sharedPreferences.commit()
    }



    fun getThisWeekInMillis() : List<Long>{

        var weekInMillis = mutableListOf<Long>()
        val cal: Calendar = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0) // ! clear would not reset the hour of day !

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek())

        System.out.println("Start of this week:       " + cal.getTime())
        System.out.println("Start of this week: ... in milliseconds:      " + cal.getTimeInMillis())

        weekInMillis.add(cal.timeInMillis)

        // start of the next week

        // start of the next week
        cal.add(Calendar.WEEK_OF_YEAR, 1)
        System.out.println("Start of the next week:   " + cal.getTime())
        System.out.println("Start of the next week: ... in milliseconds:      " + cal.getTimeInMillis())

        weekInMillis.add(cal.timeInMillis)

        return weekInMillis
    }

    fun getThisMonthInMillis() : List<Long>{

        var monthInMillis = mutableListOf<Long>()

        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)


        cal[Calendar.DAY_OF_MONTH] = 1
        System.out.println("Start of the month:       " + cal.time)
        System.out.println("Start ... in milliseconds:      " + cal.timeInMillis)

        monthInMillis.add(cal.timeInMillis)
// get start of the next month

// get start of the next month
        cal.add(Calendar.MONTH, 1)
        System.out.println("Start of the next month:  " + cal.time)
        System.out.println("Start... in milliseconds:      " + cal.timeInMillis)
        monthInMillis.add(cal.timeInMillis)

        return monthInMillis
    }

    fun getLastMonthInMillis() : List<Long>{

        var monthInMillis = mutableListOf<Long>()

        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0 // ! clear would not reset the hour of day !

        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)

        cal.add(Calendar.MONTH, -1)
        cal[Calendar.DAY_OF_MONTH] = 1
        System.out.println("Start of the month:       " + cal.time)
        System.out.println("Start ... in milliseconds:      " + cal.timeInMillis)

        monthInMillis.add(cal.timeInMillis)

        var max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.add(Calendar.DAY_OF_MONTH, max)

        System.out.println("Start of the next month:  " + cal.time)
        System.out.println("Start... in milliseconds:      " + cal.timeInMillis)
        monthInMillis.add(cal.timeInMillis)

        return monthInMillis
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String? {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }




}