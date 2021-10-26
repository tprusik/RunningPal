package com.example.runningpal.db

data class User (

        val email: String,
    val nick:String,
    val uid : String

)
{
    constructor() : this("","","")

}
