package ag.sokolov.smsrelay.service

import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var telegramBotApi: TelegramBotApi

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(
        context: Context,
        intent: Intent?
    ) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION == intent?.action) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            val sender = messages[0].originatingAddress
            val body = messages.joinToString { it.messageBody }

            val relayedMessage = listOf(sender, body).joinToString("\n\n")

            coroutineScope.launch {
                telegramBotApi.sendMessage(relayedMessage)
            }
        }
    }
}
