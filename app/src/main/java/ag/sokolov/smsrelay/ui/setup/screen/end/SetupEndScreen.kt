package ag.sokolov.smsrelay.ui.setup.screen.end

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SetupEndScreen(onFinished: () -> Unit) {
    Button(onClick = onFinished) {
        Text(text = "To statistics")
    }
}
