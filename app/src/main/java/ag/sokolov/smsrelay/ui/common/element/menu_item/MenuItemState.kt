package ag.sokolov.smsrelay.ui.common.element.menu_item

data class MenuItemState(
    val isEnabled: Boolean = true,
    val description: String? = "Loading...",
    val showWarning: Boolean = false
)
