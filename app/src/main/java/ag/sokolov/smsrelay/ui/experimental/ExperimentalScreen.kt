package ag.sokolov.smsrelay.ui.experimental

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ExperimentalScreen(
    viewModel: ExperimentalScreenViewModel = hiltViewModel()
) {
    Column {
        OutlinedTextField(
            value = viewModel.verificationCode.value,
            onValueChange = { verificationCode: String -> viewModel.onValueChange(verificationCode) })
        Button(onClick = { viewModel.onClick() }) {
            Text(text = "Submit")
        }
        Text(text = viewModel.result.value)
        Button(onClick = { viewModel.onClick2() }) {
            Text(text = "Submit")
        }
        Text(text = viewModel.result2.value)
        OutlinedTextField(value = viewModel.message.value, onValueChange = { message: String -> viewModel.onValueChange2(message) })
        Button(onClick = { viewModel.onClick3() }) {
            Text(text = "Submit")
        }
        Text(text = viewModel.result3.value)
    }
}
