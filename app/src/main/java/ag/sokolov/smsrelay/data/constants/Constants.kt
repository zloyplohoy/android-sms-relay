package ag.sokolov.smsrelay.data.constants

import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data object Constants {
    val TELEGRAM_BOT_API_LONG_POLLING_TIMEOUT: Duration = 3.seconds
    val RECIPIENT_VERIFICATION_TIMEOUT: Duration = 10.seconds
    const val RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_ID: String = "RECIPIENT_VERIFICATION"
    const val RECIPIENT_VERIFICATION_NOTIFICATION_CHANNEL_NAME: String = "Recipient verification"
    const val RECIPIENT_VERIFICATION_NOTIFICATION_TITLE: String = "Recipient verification running"
}
