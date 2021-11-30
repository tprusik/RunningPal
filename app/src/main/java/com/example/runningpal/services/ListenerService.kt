package com.example.runningpal.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.TimeFormatException
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.runningpal.DashboardActivity
import com.example.runningpal.R
import com.example.runningpal.db.Invitation
import com.example.runningpal.others.Constants
import com.example.runningpal.others.TrackingUtility
import com.example.runningpal.ui.viewmodels.RunnersViewModel
import org.koin.android.ext.android.get
import org.koin.java.KoinJavaComponent.get
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class ListenerService : LifecycleService() {

    companion object{

     val runInvite =   MutableLiveData<Invitation>()

    }
    private lateinit var notificationBuilder : NotificationCompat.Builder
    private lateinit var viewModel : RunnersViewModel

init{

    viewModel = get()

}


    override fun onCreate() {
        super.onCreate()

        Timber.d("to ja service")

        viewModel.invitation.observe(this, Observer {

            Timber.d("serwis")
            createNotification()
        })


    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

        Timber.d("service wystartowalem")

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(){

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
            .setContentText(LocalDateTime.now().toString())
            .setContentIntent(getMainActivityPendingIntent())

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, notificationBuilder.build())
        }





    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, DashboardActivity::class.java).also {
        },
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }


}