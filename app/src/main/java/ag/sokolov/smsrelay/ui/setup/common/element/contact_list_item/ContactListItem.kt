package ag.sokolov.smsrelay.ui.setup.common.element.contact_list_item

import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ContactListItem(
    title: String,
    description: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Monogram(fromString = title)
        Column(
            verticalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.height(48.dp)
        ) {
            ContactListItemTitle(text = title)
            ContactListItemDescription(text = description)
        }
    }
}

@Composable
fun Monogram(
    fromString: String,
    modifier: Modifier = Modifier
) {
    val circleColor = MaterialTheme.colorScheme.surfaceVariant
    val firstLetter = if (fromString.isNotEmpty()) fromString[0].uppercaseChar().toString() else "?"

    Box(
        modifier = modifier.size(OutlinedTextFieldDefaults.MinHeight),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(circleColor)
        }
        Text(
            text = firstLetter,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun ContactListItemTitle(
    text: String,
    color: Color? = null
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleMedium.copy(
            lineHeightStyle = LineHeightStyle(
                alignment = LineHeightStyle.Alignment.Top, trim = LineHeightStyle.Trim.None
            )
        ),
        fontWeight = FontWeight.Normal,
        color = color ?: MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun ContactListItemDescription(
    text: String,
    color: Color? = null
) {
    Text(
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Light,
        color = color ?: MaterialTheme.colorScheme.onSurface
    )
}

@Preview
@Composable
private fun PreviewContactListItem() {
    SMSRelayTheme {
        Surface {
            ContactListItem(title = "Benedict Cumberbatch", description = "@BatchOfCumbers")
        }
    }
}
