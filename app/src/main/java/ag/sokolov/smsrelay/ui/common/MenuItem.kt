package ag.sokolov.smsrelay.ui.common

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    icon: ImageVector? = null,
    title: String,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    extraIcon: ImageVector? = null,
    onExtraClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier =
            Modifier.fillMaxWidth()
                .height(80.dp)
                .then(onClick?.let { Modifier.clickable(onClick = it) } ?: Modifier)
                .padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
            icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = "Setting icon",
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge)
                description?.let {
                    Text(
                        text = description ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Light)
                }
            }
            extraIcon?.let { extraIcon ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Spacer(
                            modifier =
                                Modifier.width(1.dp)
                                    .height(32.dp)
                                    .background(MaterialTheme.colorScheme.onBackground))
                        onExtraClick?.let { onExtraClick ->
                            IconButton(
                                onClick = { onExtraClick() },
                            ) {
                                Icon(imageVector = extraIcon, contentDescription = "")
                            }
                        }
                            ?: run {
                                Icon(
                                    imageVector = extraIcon,
                                    contentDescription = "",
                                    modifier = Modifier.padding(horizontal = 12.dp))
                            }
                    }
            } ?: Spacer(modifier = Modifier.width(0.dp))
        }
}

@Preview
@Composable
private fun PreviewItemAdd() {
    SMSRelayTheme {
        Surface {
            MenuItem(icon = Icons.Filled.Add, title = "An incredibly long name for a menu item")
        }
    }
}

@Preview
@Composable
private fun PreviewItemWithDescription() {
    SMSRelayTheme {
        Surface {
            MenuItem(
                icon = Icons.AutoMirrored.Filled.Send,
                title = "An unreasonably long menu item name",
                description = "An unreasonably long and meaningless menu item description",
                onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewItemWithDeleteButton() {
    SMSRelayTheme {
        Surface {
            MenuItem(
                icon = Icons.Filled.Person,
                title = "An unreasonably long menu item name",
                description = "An unreasonably long and meaningless menu item description",
                extraIcon = Icons.Filled.Clear,
                onExtraClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewClickableItemWithDeleteButton() {
    SMSRelayTheme {
        Surface {
            MenuItem(
                icon = Icons.Filled.Person,
                title = "An unreasonably long menu item name",
                description = "An unreasonably long and meaningless menu item description",
                onClick = {},
                extraIcon = Icons.Filled.Clear,
                onExtraClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewClickableItemWithWarningExtraIcon() {
    SMSRelayTheme {
        Surface {
            MenuItem(
                icon = Icons.Filled.Person,
                title = "An unreasonably long menu item name",
                description = "An unreasonably long and meaningless menu item description",
                onClick = {},
                extraIcon = Icons.Filled.Warning)
        }
    }
}

@Preview
@Composable
private fun PreviewMenuItemColumn() {
    SMSRelayTheme {
        Surface {
            Column {
                MenuItem(
                    icon = Icons.AutoMirrored.Filled.Send,
                    title = "Telegram bot",
                    description = "@sms_relay",
                )
                MenuItem(
                    icon = Icons.Filled.Person,
                    title = "Recipient",
                    description = "Aleksei Sokolov",
                    onClick = {})
                MenuItem(
                    icon = Icons.AutoMirrored.Filled.List,
                    title = "An unreasonably long menu item name",
                    description = "An unreasonably long and meaningless menu item description",
                    onClick = {},
                    extraIcon = Icons.Filled.Clear,
                    onExtraClick = {})
            }
        }
    }
}
