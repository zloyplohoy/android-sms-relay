package ag.sokolov.smsrelay.ui.setup.screen.recipient

import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object RecipientSetupScreen

fun NavController.navigateToRecipientSetup() =
    navigate(route = RecipientSetupScreen)

fun NavGraphBuilder.recipientSetupScreen(
    onContinue: () -> Unit,
    viewModel: SetupViewModel
) =
    composable<RecipientSetupScreen> {
        RecipientSetupScreen(
            onContinue = onContinue,
            viewModel = viewModel
        )
    }

@Composable
fun RecipientSetupScreen(
    onContinue: () -> Unit,
    viewModel: SetupViewModel
) {
    val state = viewModel.state
    Column {
        Text(text = "recipientState = ${state.recipientState.javaClass.canonicalName}")
        Button(onClick = { viewModel.doWork() }) {
            Text(text = "Start work")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Start telegram")
        }
        Button(onClick = onContinue) {
            Text(text = "To permissions")
        }
    }
}
