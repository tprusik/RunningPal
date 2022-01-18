package com.example.runningpal.db

data class RoomHistory (

        var timestamp : String? = null,
        var runners: List<Runner>? = mutableListOf()
)