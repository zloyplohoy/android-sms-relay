package ag.sokolov.smsrelay.domain.errors

sealed class DomainException(message: String): Exception(message) {
    class BotNotConfiguredException(message: String = "Telegram bot token not set") : DomainException(message)
    class InvalidBotApiTokenException(message: String = "Error: Telegram bot token invalid"): DomainException(message)
    class BotNetworkException(message: String = "Error: Telegram bot failed to connect"): DomainException(message)
    class UnhandledBotException(message: String = "Error: Unhandled bot exception"): DomainException(message)
}
