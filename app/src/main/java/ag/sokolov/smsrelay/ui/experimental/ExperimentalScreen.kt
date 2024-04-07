package ag.sokolov.smsrelay.ui.experimental

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ExperimentalScreen(
    viewModel: ExperimentalViewModel = hiltViewModel()
) {
    Column {
        Text(text = "API Token Input")
        TextField(value = viewModel.apiKeyTextFieldValue.value,
            onValueChange = { value: String -> viewModel.onValueChange(value) })
        Button(onClick = { viewModel.onClick() }) {
            Text(text = "Submit")
        }
        Text(text = viewModel.responseText.value)
        Button(onClick = { viewModel.onClick2() }) {
            Text(text = "Retrieve")
        }
        Text(text = viewModel.responseText2.value)
    }
}
