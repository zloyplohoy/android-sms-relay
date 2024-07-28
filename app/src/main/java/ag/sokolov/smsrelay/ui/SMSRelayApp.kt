package ag.sokolov.smsrelay.ui

import ag.sokolov.smsrelay.ui.common.AnimatedNavHost
import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.setupScreen
import ag.sokolov.smsrelay.ui.statistics.StatisticsScreen
import ag.sokolov.smsrelay.ui.statistics.statisticsScreen
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SMSRelayApp() {
    val navController = rememberNavController()

    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AnimatedNavHost(
                navController = navController,
                startDestination = SetupScreen
            ) {
                setupScreen(onFinished = navController::finishSetup)
                statisticsScreen(navController)
            }
        }
    }
}

fun NavController.finishSetup() =
    navigate(route = StatisticsScreen) {
        popUpTo(graph.startDestinationId) { inclusive = true }
        launchSingleTop = true
    }
