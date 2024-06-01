package ag.sokolov.smsrelay.ui.common

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MenuItem(
    isEnabled: Boolean = true,
    icon: ImageVector? = null,
    title: String,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    showWarning: Boolean = false,
) {
    val enabledAwareOnClick: (() -> Unit)? = if (isEnabled) onClick else null

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .then(enabledAwareOnClick?.let { Modifier.clickable(onClick = it) } ?: Modifier)
            .padding(start = 32.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)) {
            icon?.let { icon ->
                Icon(
                    imageVector = icon,
                    contentDescription = "Setting icon",
                    tint = getEnabledAwareColor(isEnabled = isEnabled))
            }
            Column(modifier = Modifier.weight(1f)) {
                Title(text = title, color = getEnabledAwareColor(isEnabled = isEnabled))
                description?.let {
                    Description(text = it, color = getEnabledAwareColor(isEnabled = isEnabled))
                }
            }
            if (showWarning) {
                Warning()
            }
        }
}

@Composable
private fun Title(text: String, color: Color?) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleLarge,
        color = color ?: MaterialTheme.colorScheme.onSurface)
}

@Composable
fun Description(text: String, color: Color?) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Light,
        color = color ?: MaterialTheme.colorScheme.onSurface)
}

@Composable
fun Warning() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Spacer(
            modifier = Modifier
                .width(1.dp)
                .height(32.dp)
                .background(MaterialTheme.colorScheme.onBackground))
        Icon(
            imageVector = Icons.Outlined.Warning,
            contentDescription = "",
            modifier = Modifier.padding(horizontal = 12.dp))
    }
}

@Composable
private fun getEnabledAwareColor(isEnabled: Boolean): Color =
    if (isEnabled) MaterialTheme.colorScheme.onSurface
    else MaterialTheme.colorScheme.onSurfaceVariant

@Preview
@Composable
private fun PreviewMenuItem2Add() {
    MaterialTheme { Surface { MenuItem(icon = Icons.Filled.Add, title = "Add a new item") } }
}

@Preview
@Composable
private fun PreviewMenuItem2AddDisabled() {
    MaterialTheme {
        Surface { MenuItem(icon = Icons.Filled.Add, title = "Add a new item", isEnabled = false) }
    }
}

@Preview
@Composable
private fun PreviewMenuItem2Filled() {
    MaterialTheme {
        Surface {
            MenuItem(
                icon = Icons.Outlined.Email,
                title = "This is an email",
                description = "It unsurprisingly resembles an envelope",
                onClick = {})
        }
    }
}

@Preview
@Composable
private fun PreviewMenuItem2FilledDisabled() {
    MaterialTheme {
        Surface {
            MenuItem(
                icon = Icons.Outlined.Email,
                title = "This is an email",
                description = "It unsurprisingly resembles an envelope",
                isEnabled = false)
        }
    }
}
