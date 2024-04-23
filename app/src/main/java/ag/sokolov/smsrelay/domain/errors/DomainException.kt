package ag.sokolov.smsrelay.domain.errors

sealed class DomainException(message: String): Exception(message) {
    class BotNotFoundException(message: String = "Telegram bot token not set") : DomainException(message)
    class BotUnauthorizedException(message: String = "Error: Telegram bot token invalid"): DomainException(message)
    class BotNetworkException(message: String = "Error: Telegram bot failed to connect"): DomainException(message)
    class UnhandledBotException(message: String = "Error: Unhandled bot exception"): DomainException(message)
}
