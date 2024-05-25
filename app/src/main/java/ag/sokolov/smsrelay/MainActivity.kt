package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.ui.settings.common.SettingsViewModel
import ag.sokolov.smsrelay.ui.settings.navigation.SettingsNavRoutes
import ag.sokolov.smsrelay.ui.settings.navigation.settingsNavGraph
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSRelayTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                        val navHostController = rememberNavController()
                        // TODO: This should not be here
                        val settingsViewModel: SettingsViewModel = hiltViewModel()
                        NavHost(
                            navController = navHostController,
                            startDestination = SettingsNavRoutes.GRAPH_ROOT) {
                                settingsNavGraph(navHostController, settingsViewModel)
                            }
                    }
            }
        }
    }
}
