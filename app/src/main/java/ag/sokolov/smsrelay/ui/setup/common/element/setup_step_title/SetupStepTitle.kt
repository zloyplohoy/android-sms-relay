package ag.sokolov.smsrelay.ui.setup.common.element.setup_step_title

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun SetupStepTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.displayLarge,
        fontWeight = FontWeight.Light,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}
