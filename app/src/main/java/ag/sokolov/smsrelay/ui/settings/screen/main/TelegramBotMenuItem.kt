package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.state.BotState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TelegramBotMenuItem(botState: BotState, onClick: (() -> Unit)?) =
    TelegramBotMenuItemContent(
        onClick = onClick,
        description =
            when (botState) {
                is BotState.Loading -> "Loading..."
                is BotState.NotConfigured -> "Not configured"
                is BotState.Configured -> botState.botName
                is BotState.Error -> botState.errorMessage
            },
        showWarning = botState is BotState.Error)

@Composable
fun TelegramBotMenuItemContent(
    onClick: (() -> Unit)? = {},
    description: String?,
    showWarning: Boolean = false
) {
    MenuItem(
        icon = Icons.AutoMirrored.Outlined.Send,
        title = "Telegram bot",
        description = description,
        onClick = onClick,
        showWarning = showWarning)
}

@Preview
@Composable
private fun PreviewTelegramBotMenuItemLoading() {
    MaterialTheme { Surface { TelegramBotMenuItem(botState = BotState.Loading, onClick = {}) } }
}

@Preview
@Composable
private fun PrevieTelegramBotMenuItemNotConfigured() {
    MaterialTheme {
        Surface { TelegramBotMenuItem(botState = BotState.NotConfigured, onClick = {}) }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotMenuItemConfigured() {
    MaterialTheme {
        Surface {
            TelegramBotMenuItem(
                botState =
                    BotState.Configured(
                        botName = "Awesome SMS bot", botUsername = "awesome_sms_bot"),
                onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotMenuItemMenuItemError() {
    MaterialTheme {
        Surface {
            TelegramBotMenuItem(
                botState = BotState.Error(errorMessage = "API token invalid"), onClick = {})
        }
    }
}
