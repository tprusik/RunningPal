package com.example.runningpal.db

data class Room (

        val id : String? = null,
        var timeToEnd : Int? = null,
        val timestamp: String? = null,
        var isStarted: Boolean = false,
        var isFinished: Boolean = false
)