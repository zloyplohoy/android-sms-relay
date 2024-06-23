package ag.sokolov.smsrelay.ui.setup.common.element.setup_step_section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SetupStepSection(
    name: String,
    content: @Composable () -> Unit = {}
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(style = MaterialTheme.typography.titleSmall, text = name)
        content()
    }
}
