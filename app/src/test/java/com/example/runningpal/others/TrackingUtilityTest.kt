package com.example.runningpal.others

import android.location.LocationManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.runningpal.services.Polyline
import com.google.android.gms.maps.model.LatLng
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import timber.log.Timber



class TrackingUtilityTest {


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()



    @Test
    fun getProperFormattedTimeStringIfMillisNotInclude() {

        Assert.assertEquals("00:00:10",TrackingUtility.getFormattedStopWatchTime(10000))
        Assert.assertEquals("00:01:40",TrackingUtility.getFormattedStopWatchTime(100000))
        Assert.assertEquals("00:16:40",TrackingUtility.getFormattedStopWatchTime(1000000))
        Assert.assertEquals("02:46:39",TrackingUtility.getFormattedStopWatchTime(9999999))

    }

    @Test
    fun getProperFormattedTimeStringIfMillisInclude() {

        Assert.assertEquals("00:00:10:00",TrackingUtility.getFormattedStopWatchTime(10000,true))
        Assert.assertEquals("00:01:40:00",TrackingUtility.getFormattedStopWatchTime(100000,true))
        Assert.assertEquals("00:16:40:00",TrackingUtility.getFormattedStopWatchTime(1000000,true))
        Assert.assertEquals("02:46:39:99",TrackingUtility.getFormattedStopWatchTime(9999999,true))

    }



    @Test
    fun distanceBetweenPolylinesIsCorrectCalculated() {


        //prawidlowa odleglosc = 156km
        var loc00 = LatLng(10.00,10.00)
        var loc01 = LatLng(10.00,10.00)

        var loc1 = LatLng(10.00,10.00)
        var loc2 = LatLng(11.00,11.00)
        var loc3 = LatLng(12.00,12.00)

        var polylines = mutableListOf<LatLng>()
        polylines.add(loc00)
        polylines.add(loc01)
        polylines.add(loc1)
        polylines.add(loc2)
        polylines.add(loc3)


       var distance =  TrackingUtility.calculatePolylineLength(polylines)

        Assert.assertEquals(156f, TrackingUtility.calculatePolylineLength(polylines))

    }


}