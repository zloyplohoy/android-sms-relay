package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.displayMedium,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
    )
}


@Preview
@Composable
private fun PreviewScreenTitle() {
    SMSRelayTheme {
        Surface {
            ScreenTitle(title = "Settings screen")
        }
    }
}
