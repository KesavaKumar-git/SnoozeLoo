package com.example.snoozeloo.core.domain.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.snoozeloo.R
import com.example.snoozeloo.core.constants.StringConstants

object NotificationUtil
{
    fun buildNotificationChannel(id: String?, channelName: String, importance: Int, visibility: Int): NotificationChannel
    {
        with(
            NotificationChannel(id, channelName, importance)
        ) {
            lockscreenVisibility = visibility
            enableVibration(false)
            setShowBadge(true)
            return this
        }
    }

    fun createChannel(context: Context, notificationChannel: NotificationChannel)
    {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }

    fun createAlarmNotification(context: Context, content: String, smallIcon: Int, action1: PendingIntent, action2: PendingIntent): Notification
    {
        return NotificationCompat
            .Builder(context, StringConstants.ALARM_CHANNEL_ID)
            .apply {
                setContentTitle(context.getString(R.string.alarm))
                setContentText(content)
                setSmallIcon(smallIcon)
                setAutoCancel(true)
                setColor(context.getColor(R.color.primary_purple))
                addAction(0, context.getString(R.string.remind_me_later), action1)
                addAction(0, context.getString(R.string.turn_off), action2)

            }
            .build()
    }

    fun postNotification(context: Context, id: Int, notification: Notification)
    {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification)
    }

    fun removeNotification(context: Context, notificationId: Int)
    {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(notificationId)
    }
}