package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun DualPurposeLinearProgressIndicator(
    progress: Float,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    if (isLoading) {
        LinearProgressIndicator(
            modifier = modifier
        )
    } else {
        LinearProgressIndicator(
            progress = { progress },
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PreviewDualPurposeLinearProgressIndicatorProgress() {
    SMSRelayTheme {
        Surface {
            DualPurposeLinearProgressIndicator(
                progress = 0.5f,
                isLoading = false,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDualPurposeLinearProgressIndicatorLoading() {
    SMSRelayTheme {
        Surface {
            DualPurposeLinearProgressIndicator(
                progress = 0.5f,
                isLoading = true,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
