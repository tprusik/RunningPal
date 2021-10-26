package com.example.runningpal.db

data class Message (

    val senderID : String,
    val message : String
)
{
    constructor() : this("","")

}

