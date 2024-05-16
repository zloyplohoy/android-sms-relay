package ag.sokolov.smsrelay.domain.errors

sealed class DomainException(): Exception() {
    class BotNotConfiguredException() : DomainException()
    class InvalidBotApiTokenException(): DomainException()
    class BotNetworkException(): DomainException()
    class RecipientUnauthorized(): DomainException()
    class UnhandledBotException(): DomainException()
}
