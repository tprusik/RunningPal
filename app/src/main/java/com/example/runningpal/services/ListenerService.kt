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
import com.example.runningpal.db.User
import com.example.runningpal.others.Constants
import com.example.runningpal.others.Constants.ACTION_ACCEPT_FRIEND
import com.example.runningpal.others.Constants.ACTION_SHOW_FRIEND_PROFILE
import com.example.runningpal.others.Constants.ACTION_SHOW_ROOM
import com.example.runningpal.others.Utils.getUserSharedPrevs
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import org.koin.android.ext.android.get
import timber.log.Timber

class ListenerService : LifecycleService() {

    companion object{
        val runInvite =   MutableLiveData<Invitation>() }

    private lateinit var notificationBuilder : NotificationCompat.Builder
    private lateinit var viewModel : RunnersViewModel
    private lateinit var user : User

init{ viewModel = get()}


    override fun onCreate() {
        super.onCreate()
        user = getUserSharedPrevs(applicationContext)

        Timber.d("to ja service")

        viewModel.runInvitation.observe(this, Observer {
            val roomID = it.idRoom
            Timber.d("serwis")
            createRoomNotification(roomID!!)
        })

        viewModel.friendInvitation.observe(this, Observer {

            if(it.senderID != null && it.receiverID != null)
            createFriendInvitationNotification(it.senderID,it.senderID)
            Timber.d("Notyfikacja")
        })

        viewModel.acceptedInvitation.observe(this, Observer {

            user.contacts!!.add(it!!)
            viewModel.updateUser(user)
            Timber.d("odebrano akceptacje" + it)
            viewModel.deleteReceivedInvitation(it)
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
            createNotificationInviteFriendChannel(notificationManager) }

        notificationBuilder = NotificationCompat.Builder(this,
                Constants.NOTIFICATION_FRIEND_INVITE_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_bottom_nav_bar_message)
                .setContentTitle("Odebrano zaproszenie do znajomych")
                .setContentText("Zaproszenie od ${name}")
                .addAction(R.drawable.ic_bottom_nav_bar_message,"Odrzuć",rejectFriendInvitationPendingIntent())
                .addAction(R.drawable.ic_bottom_nav_bar_message,"Przyjmij",acceptFriendInvitationPendingIntent(id))
                .setAutoCancel(true)
        notificationManager.notify(1,notificationBuilder.build())
    }


    fun rejectFriendInvitationPendingIntent() = PendingIntent.getActivity(
                this,
                0,
                Intent(this, DashboardActivity::class.java),
                PendingIntent.FLAG_CANCEL_CURRENT)


    fun acceptFriendInvitationPendingIntent(id : String) = PendingIntent.getActivity(
            this,
            0,
            Intent(this, DashboardActivity::class.java).also {
                it.action = ACTION_ACCEPT_FRIEND
                it.putExtra("IDFRIEND",id)
            },
            PendingIntent.FLAG_CANCEL_CURRENT
    )


     fun createRoomNotification(idRoom : String){

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationInviteRoomChannel(notificationManager) }

        notificationBuilder = NotificationCompat.Builder(this,
            Constants.NOTIFICATION_ROOM_INVITE_CHANNEL_ID)
            .setAutoCancel(true)
            .setSmallIcon(R.drawable.ic_bottom_nav_bar_message)
            .setContentTitle("Odebrano zaproszenie")
            .addAction(R.drawable.ic_bottom_nav_bar_message,"Przyjmij",acceptRunRoomPendingIntent(idRoom))
                .addAction(R.drawable.ic_bottom_nav_bar_message,"Odrzuć",rejectRunRoomPendingIntent())

        notificationManager.notify(1,notificationBuilder.build())
     }

     fun acceptRunRoomPendingIntent(idRoom : String) = PendingIntent.getActivity(
        this,
        0,
        Intent(this, DashboardActivity::class.java).also {
            it.action = ACTION_SHOW_ROOM
            Timber.d("pending + "+ idRoom)
            it.putExtra("IDROOM",idRoom)

        }, PendingIntent.FLAG_CANCEL_CURRENT)

    fun rejectRunRoomPendingIntent() = PendingIntent.getActivity(
            this,
            0,
            Intent(this, DashboardActivity::class.java).also {},
            PendingIntent.FLAG_CANCEL_CURRENT)

    @RequiresApi(Build.VERSION_CODES.O)
     fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationInviteFriendChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                Constants.NOTIFICATION_FRIEND_INVITE_CHANNEL_ID,
                Constants.NOTIFICATION_FRIEND_INVITE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationInviteRoomChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                Constants.NOTIFICATION_ROOM_INVITE_CHANNEL_ID,
                Constants.NOTIFICATION_ROOM_INVITE_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

}