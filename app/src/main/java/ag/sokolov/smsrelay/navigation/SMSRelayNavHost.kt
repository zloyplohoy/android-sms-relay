package ag.sokolov.smsrelay.navigation

import ag.sokolov.smsrelay.ui.SMSRelayAppState
import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.setupScreen
import ag.sokolov.smsrelay.ui.statistics.statisticsScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost

@Composable
fun SMSRelayNavHost(
    appState: SMSRelayAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = SetupScreen,
        modifier = modifier
    ) {
        setupScreen(navController)
        statisticsScreen(navController)
    }
}
