package ag.sokolov.smsrelay.ui.settings.navigation

import ag.sokolov.smsrelay.ui.settings.SettingsViewModel
import ag.sokolov.smsrelay.ui.settings.screen.bot.BotSettingsScreen
import ag.sokolov.smsrelay.ui.settings.screen.main.SettingsScreen
import ag.sokolov.smsrelay.ui.settings.screen.recipient.RecipientSettingsScreen
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable

object SettingsNav {
    @Serializable object Root

    @Serializable object Main

    @Serializable object Bot

    @Serializable object Recipient
}

fun NavGraphBuilder.settingsNavGraph(
    navHostController: NavHostController,
    settingsViewModel: SettingsViewModel
) {
    return navigation<SettingsNav.Root>(
        startDestination = SettingsNav.Main,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left)
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right)
        }) {
            composable<SettingsNav.Main> {
                SettingsScreen(
                    stateFlow = settingsViewModel.state,
                    navigate = navHostController::navigate,
                )
            }

            composable<SettingsNav.Bot> {
                BotSettingsScreen(
                    state = settingsViewModel.state.collectAsState().value.botState,
                    onAction = settingsViewModel::onAction,
                    onBackClick = navHostController::popBackStack)
            }

            composable<SettingsNav.Recipient> {
                RecipientSettingsScreen(
                    state = settingsViewModel.state.collectAsState().value.recipientState,
                    onAction = settingsViewModel::onAction,
                    onBackClick = navHostController::popBackStack)
            }
        }
}
