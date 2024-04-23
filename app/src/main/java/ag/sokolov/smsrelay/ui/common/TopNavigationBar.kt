package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TopNavigationBar(
    showBackButton: Boolean = false,
    onBackButtonClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .height(96.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        if (showBackButton) {
            IconButton(onClick = { onBackButtonClick() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTopNavigationBar() {
    SMSRelayTheme {
        Surface {
            TopNavigationBar(showBackButton = true)
        }
    }
}
