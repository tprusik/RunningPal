package com.example.runningpal.db

data class User (

        var email: String? = null,
        var nick:String? =  null,
        var profilePic : String? = null,
        var backgroundPic : String? = null,
        var uid : String? = null,
        var contacts: MutableList<String>? = mutableListOf()
)

