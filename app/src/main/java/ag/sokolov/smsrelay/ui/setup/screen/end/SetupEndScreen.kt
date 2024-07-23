package ag.sokolov.smsrelay.ui.setup.screen.end

import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import ag.sokolov.smsrelay.ui.setup.screen.permissions.PermissionsSetupScreen
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SetupEndScreen

fun NavController.navigateToSetupEnd() =
    navigate(route = SetupEndScreen)

fun NavGraphBuilder.setupEndScreen(
    onFinished: () -> Unit,
    viewModel: SetupViewModel
) =
    composable<SetupEndScreen> {
        SetupEndScreen(
            onFinished = onFinished
        )
    }

@Composable
fun SetupEndScreen(onFinished: () -> Unit) {
    Button(onClick = onFinished) {
        Text(text = "To statistics")
    }
}
