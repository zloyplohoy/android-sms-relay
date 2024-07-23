package ag.sokolov.smsrelay.ui.setup.screen.permissions

import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PermissionsSetupScreen

fun NavController.navigateToPermissionsSetup() =
    navigate(route = PermissionsSetupScreen)

fun NavGraphBuilder.permissionsSetupScreen(
    onContinue: () -> Unit
) =
    composable<PermissionsSetupScreen> {
        PermissionsSetupScreen(
            onContinue = onContinue
        )
    }

@Composable
fun PermissionsSetupScreen(onContinue: () -> Unit) {
    Button(onClick = onContinue) {
        Text(text = "Finish")
    }
}
