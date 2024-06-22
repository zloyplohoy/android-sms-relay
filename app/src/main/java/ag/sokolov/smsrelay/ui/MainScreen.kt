package ag.sokolov.smsrelay.ui

import ag.sokolov.smsrelay.ui.setup.SetupScreen
import ag.sokolov.smsrelay.ui.setup.setupScreen
import ag.sokolov.smsrelay.ui.statistics.statisticsScreen
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController, startDestination = SetupScreen
            ) {
                setupScreen(navController)
                statisticsScreen(navController)
            }
        }
    }
}
