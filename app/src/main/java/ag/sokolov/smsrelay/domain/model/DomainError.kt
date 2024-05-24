package ag.sokolov.smsrelay.domain.model

sealed class DomainError {
    data object BotApiTokenMissing : DomainError()

    data object BotApiTokenInvalid : DomainError()

    data object NetworkUnavailable : DomainError()

    data object RecipientIdMissing : DomainError()

    data object RecipientNotAllowed : DomainError()

    data object UnhandledError : DomainError()
}
