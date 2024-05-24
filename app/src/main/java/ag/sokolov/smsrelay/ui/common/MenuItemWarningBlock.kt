package ag.sokolov.smsrelay.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuItemWarningBlock() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Spacer(
                modifier =
                    Modifier.width(1.dp)
                        .height(32.dp)
                        .background(MaterialTheme.colorScheme.onBackground))
            Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 12.dp))
        }
}
