package ag.sokolov.smsrelay.data.sources.remote.api.telegram_bot.dto

import kotlinx.serialization.Serializable

// https://core.telegram.org/bots/api#message

@Serializable data class TelegramMessageDto(val from: TelegramUserDto?, val text: String?)
