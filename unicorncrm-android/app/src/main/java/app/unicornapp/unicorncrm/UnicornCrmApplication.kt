package app.unicornapp.unicorncrm

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import timber.log.Timber


class UnicornCrmApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        /* TODO-FIXME
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "ibeacon_scanning_channel",
                "Scanning Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



    }
}