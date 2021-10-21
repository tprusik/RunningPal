package com.example.runningpal

data class User (

        val email: String,
    val nick:String,
    val uid : String

)
{
    constructor() : this("","","")

}
