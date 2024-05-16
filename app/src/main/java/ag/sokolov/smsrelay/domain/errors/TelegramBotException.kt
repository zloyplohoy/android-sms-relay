package ag.sokolov.smsrelay.domain.errors

sealed class TelegramBotException(): Exception() {
    class BotApiTokenMissing() : TelegramBotException()
    class BotApiTokenInvalid(): TelegramBotException()
    class NetworkUnavailable(): TelegramBotException()
    class RecipientNotAllowed(): TelegramBotException()
    class UnhandledException(): TelegramBotException()
}
