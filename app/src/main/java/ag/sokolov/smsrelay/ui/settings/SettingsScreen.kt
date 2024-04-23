package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.common.SettingsItem
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ScreenTitle(title = "Settings")
        SettingsItem(icon = Icons.AutoMirrored.Outlined.Send,
            title = "Telegram bot",
            subtitle = "Not configured",
            onClick = { navController.navigate("telegram_bot_settings") }
        )
        SettingsItem(icon = Icons.Outlined.Person,
            title = "Telegram recipient",
            subtitle = "Not configured",
            onClick = { navController.navigate("telegram_bot_settings") }
        )
        SettingsItem(icon = Icons.Outlined.MailOutline,
            title = "SMS permissions",
            subtitle = "Not granted",
            onClick = { navController.navigate("telegram_bot_settings") }
        )
    }
}
