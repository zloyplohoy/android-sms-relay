//package ag.sokolov.smsrelay.domain.use_case.add_telegram_recipient
//
//import ag.sokolov.smsrelay.data.telegram_bot_api.TelegramBotApi
//import ag.sokolov.smsrelay.domain.repository.ConfigurationRepository
//import ag.sokolov.smsrelay.domain.service.add_recipient.AddRecipientService
//import android.content.Context
//import android.content.Intent
//import android.os.Build
//import android.util.Log
//import androidx.annotation.RequiresApi
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Inject
//
//class AddTelegramRecipientUseCase
//@Inject constructor(
//    @ApplicationContext private val appContext: Context,
//    private val configurationRepository: ConfigurationRepository,
//    private val telegramBotApi: TelegramBotApi
//) {
//    @RequiresApi(Build.VERSION_CODES.O)
//    suspend operator fun invoke(): Result<Unit> {
//        val serviceIntent = Intent(appContext, AddRecipientService::class.java)
//        Log.d("TAG", "invoke: Starting service")
//        appContext.startForegroundService(serviceIntent)
//        return Result.success(Unit)
//    }
//        configurationRepository.setTelegramRecipientId(56670355)
    //        runCatching {
    //            // TODO:
    //            // 3. Save recipient chat ID
    //            // 4. Return success if a message contains the verification code
    //            val botApiToken : String = getBotApiTokenOrThrow()
    //            withTimeout(verificationTimeout) {
    //                while (true) {
    //                    // TODO: parametrize long polling timeout
    //                    telegramBotApiRepository.getMessages(botApiToken,
    // 3.seconds).getOrThrow().forEach { privateChatMessage ->
    //                        if (isValidVerificationMessage(privateChatMessage, verificationCode))
    // {
    //                            // TODO: Save recipient ID
    //                            configurationRepository.setRecipientId(privateChatMessage.from.id)
    //                            return@withTimeout
    //                        }
    //                    }
    //                }
    //            }
    //        }

    //    private suspend fun getBotApiTokenOrThrow(): String =
    //        configurationRepository.getBotApiToken().getOrThrow()

    //    private fun isValidVerificationMessage(
    //        message: TelegramPrivateChatMessage,
    //        verificationCode: String
    //    ): Boolean {
    //        return message.text == "/start $verificationCode"
    //    }
//}
