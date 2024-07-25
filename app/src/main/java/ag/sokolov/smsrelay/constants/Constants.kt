package ag.sokolov.smsrelay.constants

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data object Constants {
    val RECIPIENT_VERIFICATION_TIMEOUT: Duration = 10.seconds
    val RECIPIENT_VERIFICATION_TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT: Duration = 3.seconds
    const val RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID: String = "RECIPIENT_VERIFICATION"
    const val RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_NAME: String = "Recipient verification"
}
