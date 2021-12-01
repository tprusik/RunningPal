package com.example.runningpal.db

data class Runner (

        val id : String? = null,
        var idRoom : String? = null,
        val name : String? = null,
        val pic : String? = null,
        var avgSpeedKmh : Float = 0f,
        var distanceMetres : Int = 0,

        )