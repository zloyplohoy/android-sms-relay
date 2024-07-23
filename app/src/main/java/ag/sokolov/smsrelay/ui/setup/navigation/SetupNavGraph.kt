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

fun NavController.navigateToRecipientSetup() =
    navigate(route = SetupDestination.RECIPIENT)

fun NavController.navigateToPermissionsSetup() =
    navigate(route = SetupDestination.PERMISSIONS)

fun NavController.navigateToSetupEnd() =
    navigate(route = SetupDestination.END)
