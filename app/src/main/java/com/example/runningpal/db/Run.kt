package com.example.runningpal.db

data class Run (

      var id : String? = null,
      var routePic : String? = null,
     var timestamp : Long = 0L,
     var avgSpeedKmh : Float = 0f,
     var distanceMetres : Int = 0,
     var timeInMilis : Long = 0L,
     var caloriesBurned : Int = 0

)
