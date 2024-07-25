package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.domain.model.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID
import ag.sokolov.smsrelay.domain.model.Constants.RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_NAME
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.S_V2
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

// TODO: Reformat and rearrange
@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        // On API < 32 WorkManager starts expedited workers
        // as foreground services which require notifications
        if (SDK_INT < S_V2) {
            createRecipientVerificationNotificationChannel()
        }
    }

    companion object {
        val RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL = NotificationChannel(
            RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID,
            RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
    }

    private fun createRecipientVerificationNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL)
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
