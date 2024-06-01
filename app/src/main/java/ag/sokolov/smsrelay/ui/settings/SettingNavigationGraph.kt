package ag.sokolov.smsrelay.ui.settings

import ag.sokolov.smsrelay.ui.settings.screen.bot.BotSettingsScreen
import ag.sokolov.smsrelay.ui.settings.screen.main.SettingsScreen
import ag.sokolov.smsrelay.ui.settings.screen.recipient.RecipientSettingsScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation

object SettingsNavRoutes {
    const val GRAPH_ROOT = "settings"
    const val MAIN = "$GRAPH_ROOT/main"
    const val BOT = "$GRAPH_ROOT/bot"
    const val RECIPIENT = "$GRAPH_ROOT/recipient"
    const val PERMISSIONS = "$GRAPH_ROOT/permissions"
}

fun NavGraphBuilder.settingsNavGraph(
    navHostController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    navigation(route = SettingsNavRoutes.GRAPH_ROOT, startDestination = SettingsNavRoutes.MAIN) {
        composable(
            route = SettingsNavRoutes.MAIN,
            exitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popEnterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
                SettingsScreen(
                    state = settingsViewModel.state.collectAsState().value,
                    navigate = navHostController::navigate,
                )
            }
        composable(
            route = SettingsNavRoutes.BOT,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
                BotSettingsScreen(
                    state = settingsViewModel.state.collectAsState().value.botState,
                    onAction = settingsViewModel::onAction,
                    onBackClick = navHostController::popBackStack)
            }

        composable(
            route = SettingsNavRoutes.RECIPIENT,
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
            }) {
                RecipientSettingsScreen(
                    state = settingsViewModel.state.collectAsState().value.recipientState,
                    onAction = settingsViewModel::onAction,
                    onBackClick = navHostController::popBackStack)
            }
    }
}
