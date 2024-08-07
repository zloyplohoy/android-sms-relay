package ag.sokolov.smsrelay.domain.model

sealed class DomainError {
    data object NetworkUnavailable : DomainError()
    data object NetworkError : DomainError()
    data object BotApiTokenMissing : DomainError()
    data object BotApiTokenInvalid : DomainError()
    data object RecipientInvalid : DomainError()
    data object UnhandledError : DomainError()
}
