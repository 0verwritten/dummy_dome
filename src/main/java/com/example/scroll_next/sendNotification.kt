package com.example.scroll_next

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import androidx.core.app.NotificationCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.scroll_next.MainActivity
import com.example.scroll_next.R


private val NOTIFICATION_ID = 0
//private val REQUEST_CODE = 0
//private val FLAGS = 0

@SuppressLint("WrongConstant")
fun NotificationManager.sendNotification(title: String, messageBody: String, channelName: String, applicationContext: Context) {

    val NotificationID = (1..1000).random();

    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NotificationID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val eggImage  = (ResourcesCompat.getDrawable(applicationContext.resources, R.drawable.ic_launcher_background, null) as VectorDrawable).toBitmap();

//    val snoozeIntent = Intent(applicationContext, MainActivity::class.java)
//    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
//        applicationContext,
//        REQUEST_CODE,
//        snoozeIntent,
//        FLAGS
//    )

    val builder = NotificationCompat.Builder(
        applicationContext,
        "noto_channel"
    ).setSmallIcon(R.drawable.ic_launcher_background)
        .setContentTitle(
            title
        )
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
//        .addAction(
//            R.drawable.ic_notifications,
//            "action",
//            snoozePendingIntent
//        )

        .setPriority(NotificationCompat.PRIORITY_HIGH)
    notify(NotificationID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}