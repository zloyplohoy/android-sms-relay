package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.element.menu_item.MenuItem
import ag.sokolov.smsrelay.ui.common.element.menu_item.MenuItemState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TelegramRecipientMenuItem(state: MenuItemState, onClick: (() -> Unit)? = null) =
    MenuItem(
        isEnabled = state.isEnabled,
        icon = Icons.Outlined.Person,
        title = "Recipient",
        description = state.description,
        onClick = onClick,
        showWarning = state.showWarning)

@Preview
@Composable
private fun TelegramRecipientMenuItemLoading() {
    MaterialTheme { Surface { TelegramRecipientMenuItem(state = MenuItemState(), onClick = {}) } }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemNotConfigured() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                state = MenuItemState(description = "Not configured"), onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemConfigured() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                state = MenuItemState(description = "Aleksei Sokolov"), onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemDisabled() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                state = MenuItemState(isEnabled = false, description = "Currently offline"),
                onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemBotError() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                state = MenuItemState(isEnabled = false, description = "API token invalid"),
                onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemRecipientError() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                state = MenuItemState(description = "Bot blocked by recipient"), onClick = {})
        }
    }
}
