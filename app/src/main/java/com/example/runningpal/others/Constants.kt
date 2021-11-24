package com.example.runningpal.others

import android.graphics.Color

object Constants {

    const val ACTION_SHOW_FRIEND_PROFILE = "ACTION_SHOW_FRIEND_PROFILE"
    const val  ACTION_SHOW_USER_PROFILE = "ACTION_SHOW_USER_PROFILE"

    const val REQUEST_CODE_LOCATION_PERMISSION = 0

    const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
    const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
    const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"

    const val LOCATION_UPDATE_INTERVAL = 5000L
    const val FASTEST_LOCATION_INTERVAL = 2000L

    const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"
    const val NOTIFICATION_CHANNEL_ID  = "notification_channel_id"
    const val NOTIFICATION_CHANNEL_NAME = "Tracking"
    const val NOTIFICATION_ID = 1

    const val POLYLINE_COLOR = Color.BLACK
    const val POLYLINE_WIDTH = 8f
    const val MAP_ZOOM = 15f

    const val TIMER_UPDATE_INTERVAL = 50L

    const val SHARED_PREFERENCES_NAME = "shared_pref"
    const val KEY_FIRST_TIME_IN_APP = "KEY_FIRST_TIME_TOGGLE"
    const val KEY_USER_WEIGHT = "KEY_USER_WEIGHT"
    const val KEY_USER_NAME = "KEY_USER_NAME"
}