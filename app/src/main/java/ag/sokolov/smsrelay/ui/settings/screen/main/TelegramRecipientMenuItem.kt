package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.MenuItem
import ag.sokolov.smsrelay.ui.settings.state.RecipientState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TelegramRecipientMenuItem(recipientState: RecipientState, onClick: (() -> Unit)? = null) =
    when (recipientState) {
        is RecipientState.Loading ->
            TelegramRecipientMenuItemContent(description = recipientState.message)
        is RecipientState.NotConfigured ->
            TelegramRecipientMenuItemContent(onClick = onClick, description = "Not configured")
        is RecipientState.Configured ->
            TelegramRecipientMenuItemContent(
                onClick = onClick, description = recipientState.fullName)
        is RecipientState.RecipientError ->
            TelegramRecipientMenuItemContent(
                onClick = onClick, description = recipientState.errorMessage, showWarning = true)
        is RecipientState.ExternalError ->
            TelegramRecipientMenuItemContent(description = recipientState.errorMessage)
    }

@Composable
fun TelegramRecipientMenuItemContent(
    onClick: (() -> Unit)? = null,
    description: String?,
    showWarning: Boolean = false
) {
    MenuItem(
        icon = Icons.Outlined.Person,
        title = "Recipient",
        description = description,
        onClick = onClick,
        showWarning = showWarning)
}

@Preview
@Composable
private fun TelegramRecipientMenuItemLoading() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(recipientState = RecipientState.Loading(), onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemNotConfigured() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(recipientState = RecipientState.NotConfigured, onClick = {})
        }
    }
}

@Preview
@Composable
private fun TelegramRecipientMenuItemConfigured() {
    MaterialTheme {
        Surface {
            TelegramRecipientMenuItem(
                recipientState = RecipientState.Configured(fullName = "Aleksei Sokolov"),
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
                recipientState =
                    RecipientState.RecipientError(errorMessage = "Recipient blocked the bot"),
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
                recipientState = RecipientState.ExternalError(errorMessage = "Check bot settings"),
                onClick = {})
        }
    }
}
