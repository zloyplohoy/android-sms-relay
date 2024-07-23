package ag.sokolov.smsrelay.ui.setup.screen.recipient

import ag.sokolov.smsrelay.ui.setup.SetupViewModel
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
    onContinue: () -> Unit
) =
    composable<RecipientSetupScreen> {
        RecipientSetupScreen(
            onContinue = onContinue
        )
    }

@Composable
fun RecipientSetupScreen(onContinue: () -> Unit) {
    Button(onClick = onContinue) {
        Text(text = "To permissions")
    }
}
