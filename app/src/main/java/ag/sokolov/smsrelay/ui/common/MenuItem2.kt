package ag.sokolov.smsrelay.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Icon
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
fun MenuItem2(
    icon: ImageVector? = null,
    title: String,
    description: String? = null,
    onClick: (() -> Unit)? = null,
    content: @Composable (() -> Unit)? = null
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
                        text = it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Light)
                }
            }
            content?.let { it() }
        }
}

@Preview
@Composable
private fun PreviewMenuItem2Add() {
    MaterialTheme { Surface { MenuItem2(icon = Icons.Filled.Add, title = "Add a new item") } }
}

@Preview
@Composable
private fun PreviewMenuItem2Filled() {
    MaterialTheme {
        Surface {
            MenuItem2(
                icon = Icons.Outlined.Email,
                title = "This is an email",
                description = "It unsurprisingly resembles an envelope")
        }
    }
}
