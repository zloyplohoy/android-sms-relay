package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.state.MenuItemState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TelegramBotMenuItem(state: MenuItemState, onClick: (() -> Unit)? = null) =
    MenuItem(
        isEnabled = state.isEnabled,
        icon = Icons.AutoMirrored.Outlined.Send,
        title = "Telegram bot",
        description = state.description,
        onClick = onClick,
        showWarning = state.showWarning)

@Preview
@Composable
private fun PreviewTelegramBotMenuItemLoading() {
    MaterialTheme { Surface { TelegramBotMenuItem(state = MenuItemState(), onClick = {}) } }
}

@Preview
@Composable
private fun PreviewTelegramBotMenuItemNotConfigured() {
    MaterialTheme {
        Surface {
            TelegramBotMenuItem(state = MenuItemState(description = "Not configured"), onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewTelegramBotMenuDisabled() {
    MaterialTheme {
        Surface {
            TelegramBotMenuItem(
                state = MenuItemState(isEnabled = false, description = "Currently offline"),
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
                state = MenuItemState(description = "API token invalid", showWarning = true),
                onClick = {})
        }
    }
}
