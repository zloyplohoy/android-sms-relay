package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuHeader(title: String, onBackClick: (() -> Unit)? = null) {
    Column {
        Row(modifier = Modifier.height(64.dp).fillMaxWidth().padding(horizontal = 16.dp)) {
            onBackClick?.let { onBackClick ->
                IconButton(onClick = { onBackClick() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back")
                }
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 32.dp))
    }
}

@Preview
@Composable
private fun PreviewMenuHeader() {
    SMSRelayTheme { Surface { MenuHeader(title = "Settings") } }
}

@Preview
@Composable
private fun PreviewMenuHeaderWithBackButton() {
    SMSRelayTheme { Surface { MenuHeader(title = "Settings", onBackClick = {}) } }
}
