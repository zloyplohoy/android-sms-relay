package ag.sokolov.smsrelay

import ag.sokolov.smsrelay.ui.common.TopNavigationBar
import ag.sokolov.smsrelay.ui.settings.SettingsScreen
import ag.sokolov.smsrelay.ui.telegram_bot_settings.TelegramBotSettingsScreen
import ag.sokolov.smsrelay.ui.telegram_recipient_settings.TelegramRecipientSettingsScreen
import ag.sokolov.smsrelay.ui.theme.SMSRelayTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSRelayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val currentBackStackEntry = navController.currentBackStackEntryAsState()
                    val showBackButton: Boolean =
                        currentBackStackEntry.value?.destination?.id != navController.graph.startDestinationId

                    Column {
                        TopNavigationBar(
                            showBackButton = showBackButton,
                            onBackButtonClick = { navController.navigateUp() }
                        )
                        NavHost(
                            navController = navController,
                            startDestination = "settings"
                        ) {
                            composable(
                                route = "settings",
                                exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) }
                            ) { SettingsScreen(navigateToRoute = navController::navigate) }
                            composable(
                                route = "telegram_bot_settings",
                                enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(300)) },
                                popExitTransition = {slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300))}
                            ) { TelegramBotSettingsScreen() }
                            composable("telegram_recipient_settings") { TelegramRecipientSettingsScreen() }
                        }
                    }
                }
            }
        }
    }
}
