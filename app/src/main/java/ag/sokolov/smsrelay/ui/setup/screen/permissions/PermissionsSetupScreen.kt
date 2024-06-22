package ag.sokolov.smsrelay.ui.setup.screen.permissions

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PermissionsSetupScreen(onContinue: () -> Unit) {
    Button(onClick = onContinue) {
        Text(text = "Finish")
    }
}
