package ag.sokolov.smsrelay.ui.system_permissions

import ag.sokolov.smsrelay.ui.common.ScreenTitle
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SystemPermissionsSettingsScreen(
    viewModel: SystemPermissionsSettingsViewModel = hiltViewModel()
) {
    val vm = viewModel
    SystemPermissionsSettingsScreenContent()
}

@Composable
fun SystemPermissionsSettingsScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ScreenTitle(title = "Permissions")
    }
}

@Preview
@Composable
private fun PreviewSystemPermissionsSettingsScreenContent() {
    SMSRelayTheme {
        Surface {
            SystemPermissionsSettingsScreenContent()
        }
    }
}
