package ag.sokolov.smsrelay.domain.service.add_recipient

import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
import ag.sokolov.smsrelay.domain.repository.TelegramBotApiRepository
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import javax.inject.Inject

class AddRecipientService @Inject constructor(
    private val configurationRepository: ConfigurationRepository,
    private val telegramBotApiRepository: TelegramBotApiRepository
) : Service() {
    companion object {
        const val VERIFICATION_CODE = "verification_code"
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForeground() {
        val notification = NotificationCompat.Builder(this, "1").build()
        ServiceCompat.startForeground(
            this, 1, notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SHORT_SERVICE
            } else {
                0
            }
        )
    }
}
