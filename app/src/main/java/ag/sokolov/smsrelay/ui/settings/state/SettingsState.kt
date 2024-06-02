package ag.sokolov.smsrelay.ui.settings.state

import ag.sokolov.smsrelay.ui.common.element.menu_item.MenuItemState
import androidx.compose.runtime.Stable

@Stable
data class SettingsState(
    val botState: BotState = BotState.Loading,
    val recipientState: RecipientState = RecipientState.Loading(),
    val botMenuItemState: MenuItemState = MenuItemState(),
    val recipientMenuItemState: MenuItemState = MenuItemState()
)
