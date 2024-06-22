package ag.sokolov.smsrelay.ui.setup.screen.start

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SetupStartScreen(onContinue: () -> Unit) {
    Button(onClick = onContinue) {
        Text(text = "To bot configuration")
    }
}
