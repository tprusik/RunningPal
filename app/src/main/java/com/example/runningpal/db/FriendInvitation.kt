package com.example.runningpal.db

data class FriendInvitation (

        val senderID : String? = null,
        val receiverID : String? = null,
        var isAccepted :Boolean? = false

)