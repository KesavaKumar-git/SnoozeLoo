package com.example.snoozeloo.core.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.snoozeloo.core.domain.utils.AlarmConfigureManager
import com.example.snoozeloo.core.presentation.utils.RingtoneManagerUtil

class NotificationActionReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?)
    {
        if (context != null)
        {
            intent?.getIntExtra("alarm_id", -1)?.let {

                intent.getStringExtra("notify_action").let { action ->
                    if (action == "remind_later")
                    {
                        AlarmConfigureManager.snoozeAfter5Min(context = context, id = it, label = intent.getStringExtra("alarm_time")?: "")
                    }
                    else if (action == "turn_off")
                    {
                        RingtoneManagerUtil.stopRingtone()
                    }
                }
            }
        }
    }
}