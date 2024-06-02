package ag.sokolov.smsrelay.ui.settings.state

data class MenuItemState(
    val isEnabled: Boolean = true,
    val description: String? = "Loading...",
    val showWarning: Boolean = false
)
