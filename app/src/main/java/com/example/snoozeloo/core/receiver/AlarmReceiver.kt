package com.example.snoozeloo.core.receiver

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.snoozeloo.R
import com.example.snoozeloo.alarm.presentation.alarm_snooze.SnoozeActivity
import com.example.snoozeloo.core.constants.StringConstants
import com.example.snoozeloo.core.database.SnoozeLooDatabase
import com.example.snoozeloo.core.domain.utils.NotificationUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (context != null)
        {

            CoroutineScope(Dispatchers.IO).launch {

                intent?.getIntExtra("alarm_id", -1)?.let {

                    if (it != -1 && SnoozeLooDatabase.getInstance(context).alarmsDao().isAlarmActive(it))
                    {
                        val snoozeScreen = Intent(context, SnoozeActivity::class.java)
                        snoozeScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        snoozeScreen.putExtra("alarm_id", it)

                        context.startActivity(snoozeScreen)

                        postNotification(context, it, intent.getStringExtra("alarm_time") ?: "")
                    }
                }
            }
        }
    }

    private fun postNotification(context: Context, alarmId: Int, alarmTime: String)
    {
        val notificationChannel = NotificationUtil.buildNotificationChannel(
            id = StringConstants.ALARM_CHANNEL_ID,
            channelName = context.getString(R.string.mt_alarm_channel_name),
            importance = NotificationManager.IMPORTANCE_LOW,
            visibility = Notification.VISIBILITY_PUBLIC)

        NotificationUtil.createChannel(context, notificationChannel)

        val actionIntent = Intent(context, NotificationActionReceiver::class.java)
        actionIntent.putExtra("alarm_id", alarmId)
        actionIntent.putExtra("alarm_time", alarmTime)

        val pendingIntent1 = PendingIntent.getActivity(context, 1, actionIntent.putExtra("notify_action", "remind_later"), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val pendingIntent2 = PendingIntent.getActivity(context, 2, actionIntent.putExtra("notify_action", "turn_off"), PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationUtil.createAlarmNotification(
            context = context,
            content = alarmTime,
            smallIcon = R.drawable.ic_alarm,
            action1 = pendingIntent1,
            action2 = pendingIntent2)

        NotificationUtil.postNotification(context, 1, notification)
    }
}