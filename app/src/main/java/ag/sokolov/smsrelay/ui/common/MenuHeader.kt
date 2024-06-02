package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuHeader(title: String, onBackClick: (() -> Unit)? = null) {
    LargeTopAppBar(
        modifier = Modifier.padding(horizontal = 16.dp),
        navigationIcon = { onBackClick?.let { MenuHeaderBackButton(onClick = it) } },
        title = { MenuHeaderTitle(title) })
}

@Composable
fun MenuHeaderBackButton(onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
    }
}

@Composable
fun MenuHeaderTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.displaySmall)
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
