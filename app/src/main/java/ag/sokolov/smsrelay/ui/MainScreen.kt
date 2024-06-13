package ag.sokolov.smsrelay.ui

import ag.sokolov.smsrelay.ui.settings.SettingsViewModel
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNav
import ag.sokolov.smsrelay.ui.settings.navigation.settingsNavGraph
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val settingsViewModel: SettingsViewModel = hiltViewModel()

    SMSRelayTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Scaffold { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = SettingsNav.Root,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    settingsNavGraph(navController, settingsViewModel)
                }
            }
        }
    }
}
