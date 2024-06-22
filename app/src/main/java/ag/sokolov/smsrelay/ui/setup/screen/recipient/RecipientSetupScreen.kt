package ag.sokolov.smsrelay.ui.setup.screen.recipient

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RecipientSetupScreen(onContinue: () -> Unit) {
    Button(onClick = onContinue) {
        Text(text = "To permissions")
    }
}
