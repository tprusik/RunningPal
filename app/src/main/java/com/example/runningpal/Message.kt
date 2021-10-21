package com.example.runningpal

data class Message (

    val senderID : String,
    val message : String
)
{
    constructor() : this("","")

}

