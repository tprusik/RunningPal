package com.example.runningpal.db

data class Runner (

        val id : String? = null,
        var idRoom : String? = null,
        val name : String? = null,
        val pic : String? = null,
        var avgSpeedKmh : Float = 0f,
        var distanceMetres : Int = 0,
        var distanceProcess: List<Int>? = mutableListOf(),
        var timeProcess: List<Long>? = mutableListOf(),
        var placeProcess: List<Int>? = mutableListOf(),
        var avgSpeedProcess: List<Float>? = mutableListOf()

)