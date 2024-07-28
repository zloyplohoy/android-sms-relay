package ag.sokolov.smsrelay.ui.setup.screen.recipient

import ag.sokolov.smsrelay.ui.setup.RecipientState
import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object RecipientSetupScreen

fun NavController.navigateToRecipientSetup() =
    navigate(route = RecipientSetupScreen)

fun NavGraphBuilder.recipientSetupScreen(
    viewModel: SetupViewModel,
    onContinue: () -> Unit
) =
    composable<RecipientSetupScreen> {
        val state by viewModel.stateFlow.collectAsStateWithLifecycle()

        RecipientSetupScreen(
            state = state.recipientState,
            onContinue = onContinue,
            onRecipientRegistrationStarted = viewModel::onRecipientRegistrationStarted,
            onRecipientReset = viewModel::onRecipientReset,
        )
    }

@Composable
fun RecipientSetupScreen(
    state: RecipientState,
    onContinue: () -> Unit,
    onRecipientRegistrationStarted: () -> Unit,
    onRecipientReset: () -> Unit
) {
    Column {
        Text(text = "recipientState = $state")
        Button(onClick = { onRecipientRegistrationStarted() }) {
            Text(text = "Start work")
        }
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Start telegram")
        }
        Button(onClick = { onRecipientReset() }) {
            Text(text = "Delete recipient")
        }
        Button(onClick = onContinue) {
            Text(text = "To permissions")
        }
    }
}
