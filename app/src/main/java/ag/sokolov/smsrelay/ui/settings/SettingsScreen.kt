package ag.sokolov.smsrelay.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column {
        Button(onClick = { navController.navigate("telegram_bot_settings") }) {
            Text(text = "To telegram bot settings")
        }
    }
}
