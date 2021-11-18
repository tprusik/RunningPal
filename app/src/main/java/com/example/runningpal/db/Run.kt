package com.example.runningpal.db

import android.graphics.Bitmap

data class Run (
      var id : String? = null,
     var timestamp : Long = 0L,
     var avgSpeedKmh : Float = 0f,
     var distanceMetres : Int = 0,
     var timeInMilis : Long = 0L,
     var caloriesBurnt : Int = 0
)
