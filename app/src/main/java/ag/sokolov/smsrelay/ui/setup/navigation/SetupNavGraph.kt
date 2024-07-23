package ag.sokolov.smsrelay.ui.setup.navigation

import ag.sokolov.smsrelay.ui.setup.SetupViewModel
import ag.sokolov.smsrelay.ui.setup.screen.bot.BotSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.bot.botSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.end.SetupEndScreen
import ag.sokolov.smsrelay.ui.setup.screen.permissions.PermissionsSetupScreen
import ag.sokolov.smsrelay.ui.setup.screen.recipient.RecipientSetupScreen
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed interface SetupDestination {
    @Serializable
    object ROOT

    @Serializable
    object RECIPIENT

    @Serializable
    object PERMISSIONS

    @Serializable
    object END
}

fun NavGraphBuilder.setupScreenContent(
    setupNavController: NavController,
    viewModel: SetupViewModel,
    setLoadingState: (Boolean) -> Unit,
    onFinished: () -> Unit
) =
    navigation<SetupDestination.ROOT>(startDestination = BotSetupScreen) {
        botSetupScreen(
            onContinue = setupNavController::navigateToRecipientSetup,
            setLoadingState = setLoadingState,
            viewModel = viewModel
        )
        composable<SetupDestination.RECIPIENT> {
            RecipientSetupScreen(onContinue = setupNavController::navigateToPermissionsSetup)
        }
        composable<SetupDestination.PERMISSIONS> {
            PermissionsSetupScreen(onContinue = setupNavController::navigateToSetupEnd)
        }
        composable<SetupDestination.END> {
            SetupEndScreen(onFinished = onFinished)
        }
    }


fun NavController.navigateToRecipientSetup() =
    navigate(route = SetupDestination.RECIPIENT)

fun NavController.navigateToPermissionsSetup() =
    navigate(route = SetupDestination.PERMISSIONS)

fun NavController.navigateToSetupEnd() =
    navigate(route = SetupDestination.END)
