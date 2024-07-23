package ag.sokolov.smsrelay.ui.setup.navigation

import androidx.navigation.NavController
import kotlinx.serialization.Serializable

@Serializable
sealed interface SetupDestination {
    @Serializable
    object ROOT

    @Serializable
    object END
}

fun NavController.navigateToSetupEnd() =
    navigate(route = SetupDestination.END)
