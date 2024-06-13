package ag.sokolov.smsrelay.ui.settings.screen.main

import ag.sokolov.smsrelay.ui.common.element.menu_item.MenuItemState
import ag.sokolov.smsrelay.ui.common.element.screen_top_bar.ScreenTopBar
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNav
import ag.sokolov.smsrelay.ui.settings.state.SettingsState
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow

@Composable
fun SettingsScreen(
    stateFlow: StateFlow<SettingsState>,
    navigate: (Any) -> Unit = {}
) {
    val state = stateFlow.collectAsStateWithLifecycle()
    val botMenuItemState = state.value.botMenuItemState
    val recipientMenuItemState = state.value.recipientMenuItemState
    SettingsScreenContent(botMenuItemState = botMenuItemState, recipientMenuItemState, navigate)
}

@Composable
fun SettingsScreenContent(
    botMenuItemState: MenuItemState,
    recipientMenuItemState: MenuItemState,
    navigate: (Any) -> Unit = {}
) =
    Column(modifier = Modifier.fillMaxWidth()) {
        ScreenTopBar(title = "Settings")
        TelegramBotMenuItem(state = botMenuItemState, onClick = { navigate(SettingsNav.Bot) })
        TelegramRecipientMenuItem(
            state = recipientMenuItemState,
            onClick = { navigate(SettingsNav.Recipient) })
    }

@Preview
@Composable
private fun PreviewSettingsScreenLoading() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreenContent(
                botMenuItemState = MenuItemState(), recipientMenuItemState = MenuItemState()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreenConfigured() {
    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            SettingsScreenContent(
                botMenuItemState = MenuItemState(description = "Awesome SMS bot"),
                recipientMenuItemState = MenuItemState(description = "Aleksei Sokolov")
            )
        }
    }
}
