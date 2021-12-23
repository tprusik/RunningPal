package com.example.runningpal.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.runningpal.activities.DashboardActivity
import com.example.runningpal.R
import com.example.runningpal.db.Invitation
import com.example.runningpal.others.Constants
import com.example.runningpal.others.Constants.ACTION_SHOW_FRIEND_PROFILE
import com.example.runningpal.others.Constants.ACTION_SHOW_ROOM
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import org.koin.android.ext.android.get
import timber.log.Timber

class ListenerService : LifecycleService() {

    companion object{
        val runInvite =   MutableLiveData<Invitation>() }

    private lateinit var notificationBuilder : NotificationCompat.Builder
    private lateinit var viewModel : RunnersViewModel

init{ viewModel = get()}


    override fun onCreate() {
        super.onCreate()

        Timber.d("to ja service")

        viewModel.runInvitation.observe(this, Observer {
            val roomID = it.idRoom
            Timber.d("serwis")
            createNotification(roomID!!)
        })

        viewModel.friendInvitation.observe(this, Observer {

            createFriendInvitationNotification(it.senderID!!,it.senderID!!)

        })

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        Timber.d("service wystartowalem")

    }

    fun createFriendInvitationNotification(name : String,id : String){

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationBuilder = NotificationCompat.Builder(this,
                Constants.NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_bottom_nav_bar_message)
                .setContentTitle("Odebrano zaproszenie do znajomych")
                .setContentText("Zaproszenie od ${name}")
                .setContentIntent(getFriendInvitationPendingIntent(id))
        notificationManager.notify(1,notificationBuilder.build())

    }

    fun getFriendInvitationPendingIntent(id : String) = PendingIntent.getActivity(
            this,
            0,
            Intent(this, DashboardActivity::class.java).also {
                it.action = ACTION_SHOW_FRIEND_PROFILE
                it.putExtra("IDFRIEND",id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT
    )


     fun createNotification(idRoom : String){

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        notificationBuilder = NotificationCompat.Builder(this,
            Constants.NOTIFICATION_CHANNEL_ID
        )
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_bottom_nav_bar_message)
            .setContentTitle("Odebrano zaproszenie")
            .setContentIntent(getRunRoomPendingIntent(idRoom))

        notificationManager.notify(1,notificationBuilder.build())

    }

     fun getRunRoomPendingIntent(idRoom : String) = PendingIntent.getActivity(
        this,
        0,
        Intent(this, DashboardActivity::class.java).also {
            it.action = ACTION_SHOW_ROOM
            Timber.d("pending + "+ idRoom)
            it.putExtra("IDROOM",idRoom)
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
     fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}