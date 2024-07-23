package ag.sokolov.smsrelay.ui.setup.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
sealed interface SetupDestination {
    @Serializable
    object ROOT

    @Serializable
    object PERMISSIONS

    @Serializable
    object END
}

fun NavController.navigateToPermissionsSetup() =
    navigate(route = SetupDestination.PERMISSIONS)

fun NavController.navigateToSetupEnd() =
    navigate(route = SetupDestination.END)
