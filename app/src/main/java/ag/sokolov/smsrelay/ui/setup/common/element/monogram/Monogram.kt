package ag.sokolov.smsrelay.ui.setup.common.element.monogram

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Monogram(
    fromString: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    val localDensity = LocalDensity.current
    val circleColor = MaterialTheme.colorScheme.primaryContainer
    val firstLetter = if (fromString.isNotEmpty()) fromString[0].uppercaseChar().toString() else "?"
    var width by rememberSaveable { mutableFloatStateOf(0f) }

    Box(modifier = modifier
        .width(width.dp)
        .onGloballyPositioned {
            with(localDensity) {
                width = it.size.height.toDp().value
            }
        }, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(circleColor)
        }
        Text(text = firstLetter, style = textStyle, color = MaterialTheme.colorScheme.primary)
    }
}

@Preview
@Composable
private fun PreviewMonogram() {
    SMSRelayTheme {
        Surface {
            Monogram(fromString = "test", textStyle = MaterialTheme.typography.headlineSmall)
        }
    }
}
