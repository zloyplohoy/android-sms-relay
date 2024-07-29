package ag.sokolov.smsrelay.service

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var telegramBotApi: TelegramBotApi

    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            messages.forEach { message ->
                CoroutineScope(Dispatchers.IO).launch {
                    telegramBotApi.sendMessage(message.toTelegramMessage())
                }
            }
        }
    }

    private fun SmsMessage.toTelegramMessage() =
        """
            ${this.originatingAddress}

            ${this.messageBody}
        """.trimIndent()
}
