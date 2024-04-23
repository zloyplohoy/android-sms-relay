package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScreenText(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    )
}

@Preview
@Composable
private fun PreviewScreenText() {
    SMSRelayTheme {
        Surface {
            ScreenText(
                text = "In a forgotten grove, an old man discovers a luminescent flower that blooms only at midnight. Whispering ancient tales, the petals guide him through memories of youth, each a vivid burst of color and emotion. As dawn approaches, he realizes the flower isn’t just showing his past—it's revealing his future."
            )
        }
    }
}
