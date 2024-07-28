package ag.sokolov.smsrelay.ui.setup.screen.recipient

import ag.sokolov.smsrelay.ui.setup.RecipientState
import ag.sokolov.smsrelay.ui.setup.SetupLaunchedEffect
import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
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
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.launchedEffectFlow.collect {
                when (it) {
                    is SetupLaunchedEffect.NavigateToUri -> {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.uri))
                        context.startActivity(intent)
                    }
                }
            }
        }

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
