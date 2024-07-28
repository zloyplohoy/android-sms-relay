package ag.sokolov.smsrelay.ui.setup

sealed class SetupLaunchedEffect {
    data class NavigateToUri(val uri: String) : SetupLaunchedEffect()
}
