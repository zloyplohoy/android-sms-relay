package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.ui.telegram_bot_settings.TelegramBotSettingsScreen
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSRelayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController, startDestination = "telegram_bot_settings"
                    ) {
                        composable("telegram_bot_settings") { TelegramBotSettingsScreen() }
                    }
                }
            }
        }
    }
}
